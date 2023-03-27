package com.mountainbook.mountainbook.user.service

import com.mountainbook.mountainbook.user.dto.ImageDto
import com.mountainbook.mountainbook.user.entity.ProfileImage
import com.mountainbook.mountainbook.user.repository.ImageRepository
import com.mountainbook.mountainbook.user.repository.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.pathString

@Service
class ImageService(
    var imageRepository: ImageRepository,
    var userRepository: UserRepository
) {

    var log = KotlinLogging.logger() {}

    @Value("\${image.dir}")
    lateinit var directory: String

    @Transactional
    fun save(imageDto: ImageDto.ProfileImageDto, username: String): String {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        val imageByteArray = Base64.getDecoder().decode(imageDto.image)
        var fileName = "${UUID.randomUUID()}.png"
        var path = Paths.get(directory)
        val resolvedFile = path.resolve(fileName)

        Files.createDirectories(path)
        Files.write(resolvedFile, imageByteArray)

        imageRepository.findByUser(user)?.let { x ->
            x.src = resolvedFile.pathString
        } ?: run {
            imageRepository.save(ProfileImage(src = resolvedFile.pathString, user = user))
        }


        return "http://localhost:8080/image/${user.id}"
    }

    fun getProfileImage(userId: UUID): ByteArrayResource {
        val user = userRepository.findById(userId).orElseThrow { throw RuntimeException("User not found") }
        val image = imageRepository.findByUser(user) ?: throw RuntimeException("Image not found!")
        val imagePath = Paths.get(image.src)

        if (Files.exists(imagePath)) {
            var imageByte = Files.readAllBytes(imagePath)
            return ByteArrayResource(imageByte)
        } else {
            throw RuntimeException("Image not found on Computer")
        }
    }
}