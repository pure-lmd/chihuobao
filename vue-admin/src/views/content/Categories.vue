<template>
  <div class="content-page">
    <el-card class="content-card">
      <template #header>
        <div class="card-header">
          <span>分类管理示例</span>
        </div>
      </template>
      <p>这里可以维护文章分类、标签等信息。</p>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.content-page {
  padding: 20px;
}

.card-header {
  font-size: 16px;
  font-weight: 600;
}

.content-card p {
  margin: 0;
  color: #666;
  line-height: 1.6;
}
</style>

