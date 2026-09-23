<template>
  <div class="flowSheet-drag-container">
    <el-scrollbar>
      <panel />
    </el-scrollbar>
  </div>
</template>

<script>
  import Panel from './components/Panel'
  import './components/index.css'

  export default {
    name: 'FlowSheetDrag',
    components: {
      Panel,
    },
  }
</script>
