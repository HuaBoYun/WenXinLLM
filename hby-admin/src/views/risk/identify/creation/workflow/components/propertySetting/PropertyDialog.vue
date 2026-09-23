<template>
  <div class="property-dialog">
    <User
      v-if="nodeData.type === 'user'"
      :lf="lf"
      :node-data="nodeData"
      @onClose="handleClose"
    />
    <CommonProperty
      v-else
      :lf="lf"
      :node-data="nodeData"
      @onClose="handleClose"
    />
  </div>
</template>

<script>
  import CommonProperty from './CommonProperty'
  import User from './User.vue'

  export default {
    name: 'PropertyDialog',
    components: {
      CommonProperty,
      User,
    },
    props: {
      nodeData: {
        type: Object,
        default: () => {},
      },
      lf: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {}
    },
    methods: {
      handleClose() {
        this.$emit('setPropertiesFinish')
      },
    },
  }
</script>
<style>
  .property-dialog {
    padding: 20px;
  }
</style>
