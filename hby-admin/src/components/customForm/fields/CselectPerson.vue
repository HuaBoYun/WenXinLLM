<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      :style="{ width: '75%' }"
      disabled
    />
    <el-button
      :style="{ marginLeft: '10px', height: '30px' }"
      type="primary"
      @click="$refs.personRef.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <ExecutorOptions ref="personRef" @selected="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import ExecutorOptions from '@/views/contract/project/components/options/executor.vue'
  export default {
    name: 'CselectPerson',
    components: { ExecutorOptions },
    props: ['item', 'form'],
    data() {
      return {}
    },
    watch: {
      deep: true,
      form(val) {
        this.$emit('input', this.form)
      },
    },
    methods: {
      handleExecutorSelected(node) {
        this.form[this.item.attachedField] = node.realname
        this.form[this.item.field] = node.staffid
      },
    },
  }
</script>
