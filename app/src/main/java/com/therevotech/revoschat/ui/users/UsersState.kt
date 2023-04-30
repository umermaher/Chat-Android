package com.therevotech.revoschat.ui.users

import com.therevotech.revoschat.models.members.Member
import com.therevotech.revoschat.models.message.Message

data class UsersState(
    val users: List<Member> = emptyList(),
)