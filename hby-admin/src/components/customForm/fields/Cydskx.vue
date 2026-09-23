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
      @click="$refs.dyskx.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <dykx-options ref="dyskx" @selecteddykx="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import dykxOptions from '@/views/contract/financing/components/options/dykx.vue'
  export default {
    name: 'Cydskx',
    components: { dykxOptions },
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
        this.form[this.item.field] = node.nodeid
        this.form[this.item.attachedField] = node.nodecontent
        this.form.nodemoney = node.yfMoney
      },
    },
  }
</script>
