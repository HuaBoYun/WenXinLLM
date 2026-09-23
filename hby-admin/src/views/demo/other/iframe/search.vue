<template>
  <div class="iframe-search-container">
    <el-row :gutter="20">
      <el-col
        :lg="{ span: 12, offset: 6 }"
        :md="{ span: 20, offset: 2 }"
        :sm="{ span: 20, offset: 2 }"
        :xl="{ span: 12, offset: 6 }"
        :xs="24"
      >
        <el-form
          ref="form"
          label-position="top"
          label-width="100px"
          :model="form"
          :rules="rules"
          @submit.native.prevent
        >
          <el-form-item label="请输入跳转url" prop="url">
            <el-input v-model="form.url" />
          </el-form-item>
          <el-form-item label="请输入跳转后自动改名的Title" prop="title">
            <el-input v-model="form.title" />
          </el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="handleClick"
          >
            查询
          </el-button>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { isExternal } from '@/utils/validate'

  export default {
    name: 'IframeSearch',
    data() {
      const validateExternal = (rule, value, callback) => {
        if (!isExternal(value)) callback(new Error('url输入错误'))
        else callback()
      }
      return {
        rules: {
          url: [
            {
              required: true,
              message: '请输入跳转url',
              trigger: 'blur',
              validator: validateExternal,
            },
          ],
          title: [
            {
              required: true,
              message: '请输入跳转后自动改名的Title',
              trigger: 'blur',
            },
          ],
        },
        form: {
          url: 'https://www.so.com/s?ie=utf-8&fr=none&src=home_suggst_revise&q=vue-admin-beautiful&eci=&nlpv=test_zc_rank1',
          title: '360搜索',
        },
      }
    },
    methods: {
      handleClick() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$router.push({
              path: '/other/iframe/view',
              query: this.form,
            })
          }
        })
      },
    },
  }
</script>
