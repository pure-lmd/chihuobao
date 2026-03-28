<template>
  <div ref="chartRef" class="area-chart"></div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useChart } from './useChart'

const props = defineProps({
  options: {
    type: Object,
    required: true
  }
})

const chartRef = ref(null)
const { setOption } = useChart(chartRef, props.options)

watch(() => props.options, (newOptions) => {
  setOption(newOptions)
}, { deep: true })
</script>

<style scoped>
.area-chart {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
</style>
