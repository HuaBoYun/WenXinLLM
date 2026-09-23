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
      @click="$refs.peopleRef.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <ExecutorOptions ref="peopleRef" @selected="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import ExecutorOptions from './components/PeopleSelect.vue'
  export default {
    name: 'CselectPeople',
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
        const names = node.map((res) => res.realname)
        const ids = node.map((res) => res.staffid)
        this.form[this.item.attachedField] = names.toString()
        this.form[this.item.field] = ids.toString()
      },
    },
  }
</script>
