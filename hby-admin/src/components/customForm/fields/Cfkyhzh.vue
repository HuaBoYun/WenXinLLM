<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      :style="{ width: '256px' }"
      :disabled="false"
    />
    <el-button
      :style="{ marginLeft: '10px' }"
      type="primary"
      @click="$refs.yhzh.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <yhzh-options ref="yhzh" @selectedyhzh="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import yhzhOptions from '@/views/contract/financing/components/options/yhzh.vue'
  export default {
    name: 'Cfkyhzh',
    components: { yhzhOptions },
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
        this.form[this.item.attachedField] = node.bankaccount
        this.form[this.item.field] = node.bankid
      },
    },
  }
</script>
