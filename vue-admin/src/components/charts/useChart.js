import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

export function useChart(chartRef, options) {
  let chartInstance = null

  const initChart = () => {
    if (chartRef.value) {
      chartInstance = echarts.init(chartRef.value)
      chartInstance.setOption(options)
    }
  }

  const resizeChart = () => {
    chartInstance?.resize()
  }

  const setOption = (newOptions) => {
    chartInstance?.setOption(newOptions)
  }

  watch(() => options, (newOptions) => {
    setOption(newOptions)
  }, { deep: true })

  onMounted(() => {
    initChart()
    window.addEventListener('resize', resizeChart)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', resizeChart)
    chartInstance?.dispose()
  })

  return {
    setOption,
    resizeChart
  }
}
