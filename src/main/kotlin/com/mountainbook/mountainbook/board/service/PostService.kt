package com.mountainbook.mountainbook.board.service

import com.mountainbook.mountainbook.board.dto.PostDto
import com.mountainbook.mountainbook.board.entity.Post
import com.mountainbook.mountainbook.board.entity.PostTag
import com.mountainbook.mountainbook.board.entity.Tag
import com.mountainbook.mountainbook.board.repository.PostRepository
import com.mountainbook.mountainbook.board.repository.PostTagRepository
import com.mountainbook.mountainbook.board.repository.TagRepository
import com.mountainbook.mountainbook.user.repository.UserRepository
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostService(
    var userRepository: UserRepository,
    var postRepository: PostRepository,
    var tagRepository: TagRepository,
    var postTagRepository: PostTagRepository,
    var redisTemplate: RedisTemplate<String, Long>

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
        var post = Post(title = request.title, contents = request.contents, views = 0L, user = findUser)
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


    /**
     *
     * TODO
     * 1. 게시글 조회시 Count++ 후 Cache 저장소에 저장
     * 2. Scheduler를 통해서 저장된 Cache 데이터를 기반으로 Views save
     *
     */

    fun getPost(postId: Long): PostDto.Response {
        val findPost = postRepository.findById(postId).orElseGet { throw RuntimeException("User not found!") }
        val postTagListByFindPost = findPost.postTagList
        val tagSet: HashSet<String> = hashSetOf()

        postTagListByFindPost?.map { x -> x.tag?.name?.let { tagSet.add(it) } }

        val operations: ValueOperations<String, Long> = redisTemplate.opsForValue()


        operations.increment("${findPost.id}", 1L)
        return PostDto.Response(
            username = findPost.user.username,
            title = findPost.title,
            contents = findPost.contents,
            views = findPost.views,
            tagSet = tagSet
        )
    }

    fun postOrderByHits(): MutableList<PostDto.Response> {
        var findAllByOrderByHitsDesc = postRepository.findAllByOrderByViewsDesc()
        var responseList = mutableListOf<PostDto.Response>()
        findAllByOrderByHitsDesc?.let {
            for (post in findAllByOrderByHitsDesc) {
                val tagSet = getStringTagSetByPost(post)
                responseList.add(
                    PostDto.Response(
                        username = post.user.username,
                        title = post.title,
                        contents = post.contents,
                        views = post.views,
                        tagSet = tagSet
                    )
                )
            }
        }
        return responseList

    }

    fun getStringTagSetByPost(post: Post): HashSet<String> {
        val postTagList = post.postTagList
        var tagSet = hashSetOf<String>()

        postTagList?.let {
            for (postTag in postTagList) {
                postTag.tag?.let { it -> tagSet.add(it.name) }
            }
        }

        return tagSet
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
