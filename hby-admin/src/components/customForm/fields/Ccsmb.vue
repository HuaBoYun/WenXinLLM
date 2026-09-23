<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      :style="{ width: '256px' }"
      :placeholder="`请选择${item.name}`"
      disabled
    />
    <el-button
      :style="{ marginLeft: '10px' }"
      type="primary"
      @click="$refs.template.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <test-template ref="template" @selected="handleTemplateSelected" />
  </el-form-item>
</template>

<script>
  import TestTemplate from '@/views/internal/internalTest/components/options/TestTemplate.vue'
  export default {
    name: 'Ccsmb',
    components: { TestTemplate },
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
      handleTemplateSelected(node) {
        this.form[this.item.attachedField] = node.templename
        this.form[this.item.field] = node.testtemid
      },
    },
  }
</script>
