<#include "header.ftl" encoding="UTF-8">
<link rel="stylesheet" href="../styles/letter.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-list">
<#if conversations??>
            <#list conversations as conversation>
             <li id="conversation-item-10005_622873">
                <a class="letter-link" href="/msg/detail?conversationId=${conversation.conversation.conversationId!}">
                </a>
                <div class="letter-info">
                    <span class="l-time">${conversation.conversation.createdDate!?string('yyyy/MM/dd HH:mm:ss')}</span>
                    <div class="l-operate-bar">

                        <a href="/msg/detail?conversationId=${conversation.conversation.conversationId!}">
                            ${conversation.conversation.id!}
                        </a>
                    </div>
                </div>
                <div class="chat-headbox">
                <span class="msg-num">
                ${conversation.unread!}
                </span>
                    <a class="list-head">
                        <img alt="头像" src="${conversation.user.headUrl!}">
                    </a>
                </div>
                <div class="letter-detail">
                    <a title="${conversation.user.name!}" class="letter-name level-color-1">
                        ${conversation.user.name!}
                    </a>
                    <p class="letter-brief">
                        <a href="/msg/detail?conversationId=${conversation.conversation.conversationId!}">
                            ${conversation.conversation.content!}
                        </a>
                    </p>
                </div>
            </li>
            </#list>
</#if>
        </ul>

    </div>
</div>
<#include "footer.ftl" encoding="UTF-8">