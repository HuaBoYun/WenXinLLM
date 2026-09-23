<template>
  <div class="count-container">
    <div class="count-container-text">
      <vab-count
        v-if="show"
        :decimals="form.decimals"
        :duration="form.duration"
        :end-val="form.endVal"
        :prefix="form.prefix"
        :separator="form.separator"
        :start-val="form.startVal"
        :suffix="form.suffix"
      />
    </div>
    <el-form :inline="true" :model="form">
      <el-form-item label="起始值">
        <el-input-number v-model="form.startVal" @change="handleChange" />
      </el-form-item>
      <el-form-item label="最终值">
        <el-input-number v-model="form.endVal" @change="handleChange" />
      </el-form-item>
      <el-form-item label="持续时间">
        <el-input-number v-model="form.duration" @change="handleChange" />
      </el-form-item>
      <el-form-item label="小数位数">
        <el-input-number v-model="form.decimals" @change="handleChange" />
      </el-form-item>
      <el-form-item label="前缀">
        <el-input v-model="form.prefix" @change="handleChange" />
      </el-form-item>
      <el-form-item label="后缀">
        <el-input v-model="form.suffix" @change="handleChange" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import VabCount from '@/extra/VabCount'

  export default {
    name: 'Count',
    components: { VabCount },
    data() {
      return {
        show: true,
        form: {
          startVal: 0,
          endVal: 999,
          decimals: 0,
          prefix: '',
          suffix: '',
          separator: ',',
          duration: 8000,
        },
      }
    },
    created() {},
    methods: {
      handleChange() {
        this.show = false
        setTimeout(() => {
          this.show = true
        }, 300)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .count-container {
    &-text {
      height: 80px;
      margin-bottom: $base-margin;
      font-size: 60px;
      font-weight: bold;
      text-align: center;
      background: linear-gradient(to top, #77e0a0, #75c3e9);
      background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
</style>
