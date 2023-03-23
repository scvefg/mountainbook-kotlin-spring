package com.many.affection.user.service

import com.many.affection.user.dto.PostDto
import com.many.affection.user.entity.Post
import com.many.affection.user.entity.PostTag
import com.many.affection.user.entity.Tag
import com.many.affection.user.repository.PostRepository
import com.many.affection.user.repository.PostTagRepository
import com.many.affection.user.repository.TagRepository
import com.many.affection.user.repository.UserRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    var userRepository: UserRepository,
    var postRepository: PostRepository,
    var tagRepository: TagRepository,
    var postTagRepository: PostTagRepository
) {

    var log = KotlinLogging.logger() {}


    /**
     * 1. User(작성자)인지 검증하기
     * 2. Post 생성 후 저장하기
     * 3. Request로 넘어온 TagList를 가지고 EntityTagList 만들기
     * 4.
     */


    @Transactional
    fun write(username: String, request: PostDto.Request) {
        var findUser = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        var entityTagList: MutableList<Tag> = mutableListOf()
        var saveAllTagList: MutableList<Tag>
        var post = Post(title = request.title, contents = request.contents, hits = 0L, user = findUser)
        val savedPost = postRepository.save(post)

        request.tagList.map { x -> entityTagList.add(Tag(name = x)) }
        saveAllTagList = tagRepository.saveAll(entityTagList)
        savedPost.postTagList = postTagRepository.saveAll(addTags(savedPost, saveAllTagList))
    }

    @Transactional
    fun update(id: Long, username: String, request: PostDto.Request) {
        val findUser = userRepository.findByUsername(username)
        val findPost = postRepository.findById(id).orElseGet { throw RuntimeException("Post not found!") }

        findPost.title = request.title
        findPost.contents = request.contents

        if (findPost.user != findUser) throw RuntimeException("Not equal user!")


        // 새로 등록한 태그들 전부 저장 -> 없으면 만들어서
        var newTagList = request.tagList.toMutableList()
        var oldPostTagList = findPost.postTagList


        var noDuplicatedPostTagList: MutableList<PostTag>? = mutableListOf()
        var duplicatedTagList: MutableList<PostTag>? = mutableListOf()


        // 기존의 중복되지 않는 태그 필터링 후 삭제
        oldPostTagList?.forEach { postTag ->

            if (newTagList.contains(postTag.tag?.name)) {
                newTagList.remove(postTag.tag?.name)
            } else {
                noDuplicatedPostTagList?.add(postTag)
            }
        }

        var tags: MutableList<Tag> = mutableListOf()
        var deleteList: MutableList<Tag> = mutableListOf()
        if (noDuplicatedPostTagList != null) {
            for (postTag in noDuplicatedPostTagList) {
                postTag.tag?.let { deleteList.add(it) }
                postTag.tag = null
                postTag.post = null
            }
            postTagRepository.deleteAllInBatch(noDuplicatedPostTagList)
        }
        tagRepository.deleteAllInBatch(deleteList)

        val newTagEntityList: MutableList<Tag> = mutableListOf()
        newTagList.map { x -> newTagEntityList.add(Tag(name = x)) }
        val saveAllTagList = tagRepository.saveAll(newTagEntityList)
        val saveAllPostTagList = postTagRepository.saveAll(addTags(post = findPost, tagList = saveAllTagList))
        saveAllPostTagList.map { x -> findPost.postTagList?.add(x) }

    }


    @Transactional
    fun delete(id: Long, username: String) {
        val findPost = postRepository.findById(id).orElseThrow { throw RuntimeException("Post not found!") }
        if (findPost.user != userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")) {
            throw RuntimeException("Not equal user!")
        }
        postRepository.delete(findPost)
    }

    fun getPost(id: Long): PostDto.Response {
        val findPost = postRepository.findById(id).orElseGet { throw RuntimeException("User not found!") }
        val postTagListByFindPost = findPost.postTagList
        var tagSet: HashSet<String> = hashSetOf()
        postTagListByFindPost?.map { x -> x.tag?.name?.let { tagSet.add(it) } }
        return PostDto.Response(
            username = findPost.user.username,
            title = findPost.title,
            contents = findPost.contents,
            hits = findPost.hits,
            tagSet = tagSet
        )
    }


    fun addTags(post: Post, tagList: MutableList<Tag>): MutableList<PostTag> {
        var postTagList: MutableList<PostTag> = mutableListOf()

        for (tag in tagList) {
            val postTag = PostTag(post = post, tag = tag)
            tag.postTagList?.add(postTag)
            postTagList.add(postTag)
        }
        return postTagList
    }
}
