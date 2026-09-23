<template>
  <div>
    <vab-json-viewer copyable :expand-depth="5" sort :value="data" />
  </div>
</template>

<script>
  import VabJsonViewer from 'vue-json-viewer'

  export default {
    components: { VabJsonViewer },
    props: {
      graphData: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        data: [],
      }
    },
    created() {
      this.data = JSON.parse(
        JSON.stringify([
          {
            edges: this.graphData.edges,
            nodes: this.graphData.nodes,
          },
        ])
      )
    },
  }
</script>
<style scoped></style>
