<template>
  <div class="input-container">
    <el-card shadow="hover">
      <template #header>
        <span>基础用法</span>
      </template>
      <el-input v-model="input1" placeholder="请输入内容" width="“200px”" />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>禁用状态</span>
      </template>
      <el-input v-model="input2" :disabled="true" placeholder="请输入内容" />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>可清空</span>
      </template>
      <el-input v-model="input3" clearable placeholder="请输入内容" />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>密码框</span>
      </template>
      <el-input v-model="input4" placeholder="请输入内容" show-password />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>带 icon 的输入框</span>
      </template>
      <el-input
        v-model="input5"
        placeholder="请选择日期"
        suffix-icon="el-icon-date"
      />
      <el-input
        v-model="input6"
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>复合型输入框</span>
      </template>
      <el-input v-model="input7" placeholder="请输入内容" style="width: 300px">
        <template slot="prepend">Http://</template>
      </el-input>
      <el-input v-model="input8" placeholder="请输入内容" style="width: 300px">
        <template slot="append">.com</template>
      </el-input>
      <el-input v-model="input9" placeholder="请输入内容" style="width: 350px">
        <el-select slot="prepend" v-model="select" placeholder="请选择">
          <el-option label="选项1" :value="1" />
          <el-option label="选项2" :value="2" />
          <el-option label="选项3" :value="3" />
        </el-select>
        <el-button slot="append" icon="el-icon-search" />
      </el-input>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>textarea</span>
      </template>
      <el-input
        v-model="textarea"
        placeholder="请输入内容"
        :rows="2"
        type="textarea"
      />
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Input',
    data() {
      return {
        input1: '',
        input2: '',
        input3: '',
        input4: '',
        input5: '',
        input6: '',
        input7: '',
        input8: '',
        input9: '',
        select: 1,
        textarea: '',
      }
    },
  }
</script>

<style lang="scss" scoped>
  .input-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-input {
        width: 180px;

        &:first-child {
          margin-right: 10px;
          margin-bottom: 10px;
        }

        & + .el-input {
          margin-right: 10px;
          margin-bottom: 10px;
          margin-left: 0;
        }
      }

      .el-textarea {
        width: 180px;
      }

      .el-select {
        .el-input {
          width: 90px;
          margin-bottom: 0;
        }
      }
    }
  }
</style>
