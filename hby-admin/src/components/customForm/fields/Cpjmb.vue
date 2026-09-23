<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      disabled
      :style="{ width: '75%' }"
    />
    <el-button
      @click="openTemplateModal"
      style="margin-left: 10px"
      type="primary"
      :disabled="form.orgid.length === 0"
    >
      选择
    </el-button>
    <!-- <div class="el-form-item__error">*模板选定之后将不能修改*</div> -->

    <SelectTemplateModal
      ref="SelectTemplateModal"
      @selectT="selectT"
      :orgids="this.form.orgid"
    />
  </el-form-item>
</template>

<script>
  import SelectTemplateModal from '@/views/internal/evaluationManagement/components/selectTemplateModal.vue'
  export default {
    name: 'Cpjmb',
    components: { SelectTemplateModal },
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
      openTemplateModal() {
        this.$refs['SelectTemplateModal'].showEdit()
      },
      selectT(val) {
        this.$set(this.form, this.item.attachedField, val[0].TEMPLENAME)
        this.$set(this.form, this.item.field, val[0].ASSTEMID)
      },
    },
  }
</script>
