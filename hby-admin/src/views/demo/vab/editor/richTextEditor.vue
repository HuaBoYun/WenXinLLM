<template>
  <div class="editor-container">
    <el-form ref="form" label-width="100px" :model="form" :rules="rules">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" maxlength="20" />
      </el-form-item>
      <el-form-item label="所属模块" prop="module">
        <el-select v-model="form.module">
          <el-option label="新闻动态" value="1" />
          <el-option label="实时热点" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item class="vab-quill-content" label="内容" prop="content">
        <vab-quill
          ref="vab-quill"
          v-model="form.content"
          :min-height="400"
          :options="options"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleAddImg">
          模拟在富文本编辑器添加上传到文件服务器的图片url
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handlePreview">预览效果</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>
    <el-dialog
      title="预览效果"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false"
    >
      <h1 class="news-title">{{ form.title }}</h1>
      <div class="news-content" v-html="form.content" />
    </el-dialog>
    <vab-upload
      ref="vabUpload"
      :limit="50"
      name="file"
      :size="2"
      url="/upload"
    />
  </div>
</template>

<script>
  import VabQuill from '@/extra/VabQuill'
  import VabUpload from '@/extra/VabUpload'

  export default {
    name: 'RichTextEditor',
    components: { VabQuill, VabUpload },
    data() {
      return {
        borderColor: '#dcdfe6',
        dialogTableVisible: false,
        form: {
          title: '',
          module: '',
          content: '',
        },
        rules: {
          title: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'blur',
            },
          ],
          module: [
            {
              required: true,
              message: '请选择模块',
              trigger: 'change',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入内容',
              trigger: 'blur',
            },
          ],
        },
        options: {
          theme: 'snow',
          bounds: document.body,
          debug: 'warn',
          modules: {
            toolbar: {
              container: [
                ['bold', 'italic', 'underline', 'strike'],
                [{ header: [1, 2, 3, 4, 5, 6, false] }],
                [{ size: ['small', false, 'large', 'huge'] }],
                [{ color: [] }, { background: [] }],
                ['blockquote', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }],
                [{ script: 'sub' }, { script: 'super' }],
                [{ indent: '-1' }, { indent: '+1' }],
                [{ align: [] }],
                [{ direction: 'rtl' }],
                [{ font: [] }],
                ['clean'],
                ['link', 'image', 'vab-upload-image'],
              ],
              handlers: {
                'vab-upload-image': () => {
                  this.$baseConfirm(
                    '演示环境未使用真实文件服务器，故图片上传回显不会生效，开发时请修改为正式文件服务器地址',
                    '开发注意事项！！！',
                    () => {
                      this.$refs['vabUpload'].handleShow()
                    },
                    () => {
                      this.handleAddImg()
                    },
                    '模拟打开文件上传',
                    '模拟添加一张文件服务器图片'
                  )
                },
              },
            },
          },
          placeholder: '内容...',
          readOnly: false,
        },
      }
    },
    mounted() {
      // this.$nextTick(() => {
      //
      // })
    },
    methods: {
      handlePreview() {
        this.$refs['form'].validate((valid) => {
          this.$refs.form.validateField('content', () => {})
          if (valid) {
            this.dialogTableVisible = true
          }
        })
      },
      handleSave() {
        this.$refs['form'].validate((valid) => {
          this.$refs.form.validateField('content', (errorMsg) => {
            this.borderColor = '#dcdfe6'
            if (errorMsg) {
              this.borderColor = '#F56C6C'
            }
          })
          if (valid) {
            this.$baseMessage('submit!', 'success', 'vab-hey-message-success')
          }
        })
      },
      handleAddImg() {
        this.form.content +=
          '<img src="https://gitee.com/chu1204505056/image/raw/master/ewm.png" />'
      },
    },
  }
</script>

<style lang="scss" scoped>
  .editor-container {
    .news {
      &-title {
        text-align: center;
      }

      &-content {
        ::v-deep {
          p {
            line-height: 30px;

            img {
              display: block;
              margin-right: auto;
              margin-left: auto;
            }
          }
        }
      }
    }

    .vab-quill-content {
      ::v-deep {
        .el-form-item__content {
          line-height: normal;
        }
      }
    }

    @font-face {
      font-family: 'remixicon';
      src: url('../../../../../node_modules/remixicon/fonts/remixicon.eot?t=1590207869815'); /* IE9*/
      src: url('../../../../../node_modules/remixicon/fonts/remixicon.eot?t=1590207869815#iefix')
          format('embedded-opentype'),
        url('../../../../../node_modules/remixicon/fonts/remixicon.woff2?t=1590207869815')
          format('woff2'),
        url('../../../../../node_modules/remixicon/fonts/remixicon.woff?t=1590207869815')
          format('woff'),
        url('../../../../../node_modules/remixicon/fonts/remixicon.ttf?t=1590207869815')
          format('truetype'),
        url('../../../../../node_modules/remixicon/fonts/remixicon.svg?t=1590207869815#remixicon')
          format('svg');
      font-display: swap;
    }
    ::v-deep {
      .vab-quill {
        .ql-vab-upload-image {
          font-family: 'remixicon', sans-serif !important;
          font-size: 16px;
          font-weight: 580;

          &:before {
            content: '\ee46';
          }
        }
      }
    }
  }
</style>
