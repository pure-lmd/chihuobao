<template>
  <div class="chat-page">
    <el-card class="chat-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="title-area">
            <el-icon class="title-icon"><ChatDotRound /></el-icon>
            <div>
              <h2>AI 智能助手</h2>
              <p>无需接入真实后端，演示 AI 会话流程与界面交互</p>
            </div>
          </div>
          <div class="status-area">
            <el-tag type="success" effect="light">
              当前模式：模拟对话
            </el-tag>
            <el-tag type="info" effect="light">
              对话轮次：{{ totalMessages }}
            </el-tag>
          </div>
        </div>
      </template>

      <div class="chat-body">
        <div class="conversation" ref="conversationRef">
          <div
            v-for="message in messages"
            :key="message.id"
            class="message-item"
            :class="message.role"
          >
            <el-avatar
              :icon="message.role === 'ai' ? ChatRound : User"
              :class="['message-avatar', message.role]"
            />
            <div class="message-content">
              <div class="message-meta">
                <span class="name">{{ message.role === 'ai' ? 'AI 助手' : '我' }}</span>
                <span class="time">{{ message.time }}</span>
              </div>
              <div class="message-text" v-html="renderMessage(message)"></div>
            </div>
          </div>

          <transition name="fade" mode="out-in">
            <div v-if="isTyping" class="typing-indicator" key="typing">
              <el-avatar :icon="ChatRound" class="message-avatar ai" />
              <div class="typing-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
              <span class="typing-text">AI 正在思考回复...</span>
            </div>
          </transition>
        </div>

        <div class="chat-input">
          <el-input
            v-model="draft"
            type="textarea"
            :rows="4"
            resize="none"
            placeholder="请输入想咨询的问题，按 Enter 发送，Shift + Enter 换行"
            @keydown.enter.prevent="handleEnter"
          />
          <div class="action-bar">
            <div class="tips">
              <el-tag size="small" effect="dark" type="info">模拟中</el-tag>
              <span>当前为前端模拟对话，后续可替换为真实接口</span>
            </div>
            <el-button type="primary" :loading="sending" @click="handleSend">
              发送
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ChatDotRound, ChatRound, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'

const draft = ref('')
const sending = ref(false)
const isTyping = ref(false)
const conversationRef = ref(null)

const formatTime = (date) =>
  `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes()
    .toString()
    .padStart(2, '0')}`

const genId = () =>
  Math.random().toString(36).slice(2, 10) + Date.now().toString(36)

const initialMessage = {
  id: genId(),
  role: 'ai',
  content: '你好，我是你的智能助手。可以帮助你梳理项目需求、体验流程，或快速了解后台功能。\n\n试着向我提问吧！',
  time: formatTime(new Date())
}

const messages = ref([initialMessage])

const mockKnowledgeBase = [
  {
    keywords: ['登录', '注册', '账号'],
    reply:
      '关于账号相关的流程：可以前往登录页或注册页体验完整的表单校验，若有自定义需求也可以延展开发。'
  },
  {
    keywords: ['内容', '文章', '分类'],
    reply:
      '内容管理模块支持文章与分类的基础 CRUD，当前页面展示的是示例，后续可与后台 API 对接实现完整功能。'
  },
  {
    keywords: ['功能', '模块', '页面'],
    reply:
      '系统包含仪表盘、管理员管理、内容管理、个人资料等模块，可根据需求扩展更多页面。'
  },
  {
    keywords: ['部署', '上线', '环境'],
    reply:
      '本项目基于 Vite + Vue3 + Element Plus 开发，打包后可部署至任意静态资源服务器，配合后端接口即可上线。'
  }
]

const fallbackReplies = [
  '这是一个纯前端模拟的 AI 模块，实际项目中可在此处接入真实模型接口。',
  '我暂时只能基于预设知识回答问题，但你可以继续提问以体验对话流程。',
  '如果你有特定的后台管理需求，也可以告诉我，我会尝试给出方案建议。'
]

const scrollToBottom = () => {
  nextTick(() => {
    if (conversationRef.value) {
      conversationRef.value.scrollTop = conversationRef.value.scrollHeight
    }
  })
}

const timers = new Set()

const setManagedTimeout = (fn, delay) => {
  const id = setTimeout(() => {
    timers.delete(id)
    fn()
  }, delay)
  timers.add(id)
  return id
}

const clearAllTimers = () => {
  timers.forEach((id) => clearTimeout(id))
  timers.clear()
}

onBeforeUnmount(clearAllTimers)

const escapeHtml = (text) =>
  text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')

const formatToHtml = (text) => escapeHtml(text).replace(/\n/g, '<br/>')

const renderMessage = (message) =>
  formatToHtml(message?.displayContent ?? message?.content ?? '')

const appendMessage = ({ role, content = '', displayContent = null }) => {
  const message = {
    id: genId(),
    role,
    content,
    displayContent,
    time: formatTime(new Date())
  }

  messages.value.push(message)
  scrollToBottom()
  return message
}

const startTypewriter = (message, fullText) => {
  const characters = Array.from(fullText)

  if (!characters.length) {
    message.content = ''
    message.displayContent = null
    sending.value = false
    isTyping.value = false
    return
  }

  let index = 0

  const typeNext = () => {
    message.displayContent = characters.slice(0, index + 1).join('')
    scrollToBottom()

    if (index < characters.length - 1) {
      index += 1
      setManagedTimeout(typeNext, 18 + Math.random() * 35)
    } else {
      message.content = fullText
      message.displayContent = null
      sending.value = false
      isTyping.value = false
    }
  }

  setManagedTimeout(typeNext, 160)
}

const mockAIReply = (question) => {
  const lowerQuestion = question.toLowerCase()
  const matched = mockKnowledgeBase.find(({ keywords }) =>
    keywords.some((word) => lowerQuestion.includes(word))
  )

  if (matched) {
    return matched.reply
  }

  return fallbackReplies[Math.floor(Math.random() * fallbackReplies.length)]
}

const handleSend = () => {
  const text = draft.value.trim()

  if (!text) {
    ElMessage.warning('请输入内容后再发送')
    return
  }

  appendMessage({ role: 'user', content: text })
  draft.value = ''
  sending.value = true
  isTyping.value = true

  setManagedTimeout(() => {
    const answer = mockAIReply(text)
    const aiMessage = appendMessage({ role: 'ai', content: '', displayContent: '' })
    isTyping.value = false
    startTypewriter(aiMessage, answer)
  }, 900 + Math.random() * 600)
}

const handleEnter = (event) => {
  if (event.shiftKey) {
    draft.value += '\n'
    return
  }

  handleSend()
}

onMounted(scrollToBottom)

const totalMessages = computed(() => messages.value.length)

</script>

<style scoped>
.chat-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chat-card {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.title-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: rgba(30, 128, 255, 0.12);
  color: #1e80ff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title-area h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1f3b5d;
}

.title-area p {
  margin: 4px 0 0;
  color: #6b7a99;
  font-size: 14px;
}

.status-area {
  display: flex;
  align-items: center;
  gap: 8px;
}

.chat-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 640px;
  max-height: calc(100vh - 280px);
}

.conversation {
  flex: 1;
  padding: 16px;
  background: #f6f9ff;
  border-radius: 12px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.message-item {
  display: flex;
  gap: 12px;
}

.message-item.user {
  flex-direction: row-reverse;
  text-align: right;
}

.message-avatar {
  background-color: #fff;
  border: 1px solid rgba(31, 59, 93, 0.1);
}

.message-avatar.ai {
  background-color: #e8f2ff;
  color: #1e80ff;
}

.message-avatar.user {
  background-color: #1e80ff;
  color: #fff;
}

.message-item.user .message-content {
  align-items: flex-end;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-meta {
  display: flex;
  gap: 8px;
  color: #94a3b8;
  font-size: 12px;
}

.message-text {
  padding: 12px 16px;
  background: #fff;
  border-radius: 12px;
  color: #1f3b5d;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.08);
  line-height: 1.6;
}

.message-item.user .message-text {
  background: #1e80ff;
  color: #fff;
  box-shadow: 0 6px 16px rgba(30, 128, 255, 0.25);
}

.typing-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #94a3b8;
}

.typing-dots {
  display: inline-flex;
  gap: 4px;
}

.typing-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #94a3b8;
  animation: bounce 1.2s infinite ease-in-out;
}

.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

.typing-text {
  font-size: 13px;
}

.chat-input {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.tips {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #64748b;
  font-size: 13px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

@media (max-width: 1024px) {
  .message-content {
    max-width: 80%;
  }

  .chat-body {
    height: auto;
    max-height: none;
  }
}
</style>

