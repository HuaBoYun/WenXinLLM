<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      :disabled="true"
      :style="{ width: '75%' }"
    />
    <el-button
      @click="openObjectModal"
      style="margin-left: 10px"
      type="primary"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <SelectObjectModal ref="SelectObjectModal" @selectO="selectO" />
  </el-form-item>
</template>

<script>
  import SelectObjectModal from '@/views/internal/evaluationManagement/components/selectObject'
  export default {
    name: 'Cpjdx',
    components: { SelectObjectModal },
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
      openObjectModal() {
        this.$refs['SelectObjectModal'].showEdit()
      },
      selectO(val) {
        this.$set(this.form, this.item.attachedField, val.name)
        this.$set(this.form, this.item.field, val.id)
      },
    },
  }
</script>
