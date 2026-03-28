<template>
  <div class="image-preview">
    <div
      v-if="imageUrl"
      class="preview-container"
      @click="handlePreview"
    >
      <img
        :src="processedImageUrl"
        :alt="alt"
        class="preview-image"
        :style="{ width: width + 'px', height: height + 'px' }"
      />
      <div class="preview-overlay">
        <el-icon class="preview-icon">
          <ZoomIn />
        </el-icon>
      </div>
    </div>
    <div v-else class="no-image">
      <el-icon class="no-image-icon">
        <Picture />
      </el-icon>
      <span class="no-image-text">暂无图片</span>
    </div>

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="title"
      width="90%"
      :before-close="handleClose"
      class="image-dialog"
      destroy-on-close
      append-to-body
    >
      <div class="dialog-content">
        <div class="image-container">
          <img
            ref="imageRef"
            :src="processedImageUrl"
            :alt="alt"
            class="dialog-image"
            :style="imageStyle"
          />
        </div>

        <div class="toolbar">
          <el-button-group>
            <el-button @click="zoomIn" :disabled="scale >= 3" size="small">
              <el-icon><ZoomIn /></el-icon>
              放大
            </el-button>
            <el-button @click="zoomOut" :disabled="scale <= 0.2" size="small">
              <el-icon><ZoomOut /></el-icon>
              缩小
            </el-button>
            <el-button @click="rotateLeft" size="small">
              <el-icon><RefreshLeft /></el-icon>
              左转
            </el-button>
            <el-button @click="rotateRight" size="small">
              <el-icon><RefreshRight /></el-icon>
              右转
            </el-button>
            <el-button @click="reset" size="small">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-button-group>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import {
  ZoomIn,
  ZoomOut,
  RefreshLeft,
  RefreshRight,
  Refresh,
  Picture
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  imageUrl: {
    type: String,
    default: ''
  },
  alt: {
    type: String,
    default: '图片'
  },
  title: {
    type: String,
    default: '图片预览'
  },
  width: {
    type: Number,
    default: 100
  },
  height: {
    type: Number,
    default: 100
  },
  fit: {
    type: String,
    default: 'cover', // cover, contain, fill, scale-down, none
    validator: (value) => ['cover', 'contain', 'fill', 'scale-down', 'none'].includes(value)
  },
  preview: {
    type: Boolean,
    default: true
  }
})

// 响应式数据
const dialogVisible = ref(false)
const scale = ref(1)
const rotate = ref(0)
const imageRef = ref(null)
const baseUrl = import.meta.env.VITE_API_URL || ''

// 计算属性
const processedImageUrl = computed(() => {
  if (!props.imageUrl) return ''
  
  // 如果是完整的 HTTP/HTTPS 链接，直接返回
  if (props.imageUrl.startsWith('http://') || props.imageUrl.startsWith('https://')) {
    return props.imageUrl
  }
  
  // 如果是以 / 开头的路径，拼接 baseUrl
  if (props.imageUrl.startsWith('/')) {
    return baseUrl + props.imageUrl
  }
  
  // 其他情况也拼接 baseUrl（相对路径）
  return baseUrl + '/' + props.imageUrl
})

const imageStyle = computed(() => ({
  transform: `scale(${scale.value}) rotate(${rotate.value}deg)`,
  transition: 'transform 0.3s ease',
  maxWidth: '100%',
  maxHeight: '100%',
  objectFit: 'contain'
}))

// 方法
const handlePreview = () => {
  if (props.preview && props.imageUrl) {
    dialogVisible.value = true
    reset()
  }
}

const handleClose = () => {
  dialogVisible.value = false
  reset()
}

const zoomIn = () => {
  if (scale.value < 3) {
    scale.value = Math.min(scale.value + 0.2, 3)
  }
}

const zoomOut = () => {
  if (scale.value > 0.2) {
    scale.value = Math.max(scale.value - 0.2, 0.2)
  }
}

const rotateLeft = () => {
  rotate.value -= 90
}

const rotateRight = () => {
  rotate.value += 90
}

const reset = () => {
  scale.value = 1
  rotate.value = 0
}

// 暴露方法给父组件
defineExpose({
  preview: handlePreview,
  reset
})
</script>

<style scoped>
.image-preview {
  display: inline-block;
  position: relative;
}

.preview-container {
  position: relative;
  cursor: pointer;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  background: #fff;
}

.preview-container:hover {
  border-color: #ff6b35;
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.15);
}

.preview-image {
  display: block;
  object-fit: v-bind(fit);
  transition: transform 0.3s ease;
  width: 100%;
  height: 100%;
}

.preview-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.preview-container:hover .preview-overlay {
  opacity: 1;
}

.preview-icon {
  font-size: 28px;
  color: #fff;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.no-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: v-bind(width + 'px');
  height: v-bind(height + 'px');
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  color: #909399;
  background: #fafafa;
  transition: all 0.3s ease;
}

.no-image:hover {
  border-color: #c0c4cc;
  background: #f5f7fa;
}

.no-image-icon {
  font-size: 28px;
  margin-bottom: 8px;
  opacity: 0.6;
}

.no-image-text {
  font-size: 12px;
  opacity: 0.8;
}

/* 对话框样式 */
.image-dialog :deep(.el-dialog) {
  margin: 5vh auto;
  max-width: 1200px;
  border-radius: 8px;
  overflow: hidden;
}

.image-dialog :deep(.el-dialog__header) {
  padding: 16px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.image-dialog :deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.image-dialog :deep(.el-dialog__body) {
  padding: 0;
  background: #fff;
}

.dialog-content {
  display: flex;
  flex-direction: column;
  height: 75vh;
  min-height: 400px;
}

.image-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: #f8f9fa;
  position: relative;
  min-height: 300px;
}

.dialog-image {
  max-width: 90%;
  max-height: 90%;
  user-select: none;
  pointer-events: none;
  border-radius: 4px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.toolbar {
  padding: 16px 20px;
  text-align: center;
  border-top: 1px solid #e9ecef;
  background: #fff;
  flex-shrink: 0;
}

.toolbar .el-button-group {
  display: inline-flex;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  overflow: hidden;
}

.toolbar .el-button {
  margin: 0;
  border-radius: 0;
  border-left: none;
  border-right: none;
}

.toolbar .el-button:first-child {
  border-left: 1px solid #dcdfe6;
  border-radius: 6px 0 0 6px;
}

.toolbar .el-button:last-child {
  border-right: 1px solid #dcdfe6;
  border-radius: 0 6px 6px 0;
}

.toolbar .el-button:hover {
  background: #fff5f2;
  border-color: #ff6b35;
  color: #ff6b35;
  z-index: 1;
  position: relative;
}

.toolbar .el-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .image-dialog :deep(.el-dialog) {
    width: 95% !important;
    margin: 2vh auto;
  }

  .dialog-content {
    height: 85vh;
  }

  .toolbar {
    padding: 12px 16px;
  }

  .toolbar .el-button {
    padding: 8px 12px;
    font-size: 12px;
  }

  .toolbar .el-button .el-icon {
    margin-right: 4px;
  }
}

@media (max-width: 480px) {
  .image-dialog :deep(.el-dialog) {
    width: 100% !important;
    margin: 0;
    height: 100vh;
    border-radius: 0;
  }

  .dialog-content {
    height: calc(100vh - 60px);
  }

  .toolbar .el-button {
    padding: 6px 8px;
    font-size: 11px;
  }

  .toolbar .el-button span {
    display: none;
  }
}
</style>
