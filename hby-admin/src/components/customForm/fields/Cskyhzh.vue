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
      @click="$refs.yhzhsk.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <yhzhsk-options ref="yhzhsk" @selectedyhzhsk="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import yhzhskOptions from '@/views/contract/financing/components/options/yhzhsk.vue'
  export default {
    name: 'Cskyhzh',
    components: { yhzhskOptions },
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
        this.form[this.item.attachedField] = node.bankaccnum
        this.form[this.item.field] = node.bankid
      },
    },
  }
</script>
