<template>
  <div class="image-upload">
    <el-upload
        :action="uploadUrl"
        :show-file-list="false"
        :on-success="handleSuccess"
        :on-error="handleError"
        :before-upload="beforeUpload"
        :on-progress="handleProgress"
        class="upload-container"
    >
      <div v-if="imageUrl" class="image-preview">
        <img :src="imageUrl" alt="预览图片" />
        <div class="image-overlay">
          <el-icon class="overlay-icon" @click.stop="handlePreview">
            <ZoomIn />
          </el-icon>
          <el-icon class="overlay-icon" @click.stop="handleRemove">
            <Delete />
          </el-icon>
        </div>
      </div>
      <div v-else class="upload-placeholder">
        <el-icon v-if="!uploading" class="upload-icon">
          <Plus />
        </el-icon>
        <div v-if="uploading" class="upload-progress">
          <el-progress
              type="circle"
              :percentage="uploadPercent"
              :width="50"
              :stroke-width="4"
          />
        </div>
        <div class="upload-text">
          {{ uploading ? '上传中...' : '点击上传图片' }}
        </div>
      </div>
    </el-upload>

    <!-- 图片预览对话框 -->
    <el-dialog
        v-model="previewVisible"
        title="图片预览"
        width="50%"
        :before-close="handlePreviewClose"
    >
      <div class="preview-container">
        <img :src="imageUrl" alt="预览图片" style="width: 100%; height: auto;" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  uploadUrl: {
    type: String,
    default: '/api/files/upload'
  },
  maxSize: {
    type: Number,
    default: 100 // MB
  },
  acceptTypes: {
    type: Array,
    default: () => ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
  },
  width: {
    type: Number,
    default: 120
  },
  height: {
    type: Number,
    default: 120
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'success', 'error'])

// 响应式数据
const imageUrl = ref(props.modelValue)
const uploading = ref(false)
const uploadPercent = ref(0)
const previewVisible = ref(false)
const baseUrl = import.meta.env.VITE_API_URL || ''

const resolveImageUrl = (url) => {
  if (!url) {
    return ''
  }

  const isAbsoluteUrl = /^(https?:)?\/\//i.test(url)
  const isDataUrl = /^data:/i.test(url)

  if (isAbsoluteUrl || isDataUrl) {
    return url
  }

  return baseUrl + url
}

// 监听modelValue变化
watch(() => props.modelValue, (newVal) => {
  imageUrl.value = resolveImageUrl(newVal)
})

onMounted(() => {
  imageUrl.value = resolveImageUrl(props.modelValue)
})

// 上传前验证
const beforeUpload = (file) => {
  // 验证文件类型
  if (!props.acceptTypes.includes(file.type)) {
    ElMessage.error(`只能上传 ${props.acceptTypes.join('/')} 格式的图片!`)
    return false
  }

  // 验证文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB!`)
    return false
  }

  uploading.value = true
  uploadPercent.value = 0
  return true
}

// 上传进度
const handleProgress = (event) => {
  uploadPercent.value = Math.round(event.percent)
}

// 上传成功
const handleSuccess = (response) => {
  uploading.value = false
  uploadPercent.value = 0

  if (response.code === 200) {
    imageUrl.value = resolveImageUrl(response.data)
    emit('update:modelValue', response.data)
    emit('success', response.data)
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || '图片上传失败')
    emit('error', response.msg || '图片上传失败')
  }
}

// 上传失败
const handleError = (error) => {
  uploading.value = false
  uploadPercent.value = 0
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
  emit('error', '图片上传失败')
}

// 预览图片
const handlePreview = () => {
  previewVisible.value = true
}

// 关闭预览
const handlePreviewClose = () => {
  previewVisible.value = false
}

// 删除图片
const handleRemove = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要删除这张图片吗？',
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    imageUrl.value = ''
    emit('update:modelValue', '')
    ElMessage.success('图片已删除')
  } catch (error) {
    // 用户取消删除
  }
}
</script>

<style scoped>
.image-upload {
  display: inline-block;
}

.upload-container {
  position: relative;
}

.upload-container :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: 0.2s;
  width: v-bind(width + 'px');
  height: v-bind(height + 'px');
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-container :deep(.el-upload:hover) {
  border-color: #ff6b35;
}

.image-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.overlay-icon {
  font-size: 20px;
  color: #fff;
  margin: 0 8px;
  cursor: pointer;
  transition: transform 0.2s;
}

.overlay-icon:hover {
  transform: scale(1.2);
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: #8c939d;
}

.upload-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

.upload-progress {
  margin-bottom: 8px;
}

.upload-text {
  font-size: 12px;
  text-align: center;
}

.preview-container {
  text-align: center;
}
</style>
