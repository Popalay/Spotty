package com.popalay.spotty.models

data class Comment(val authorId: String = "",
                   val text: String = ""
)

data class CommentUI(val authorPhotoUrl: String = "",
                     val text: String = ""
)