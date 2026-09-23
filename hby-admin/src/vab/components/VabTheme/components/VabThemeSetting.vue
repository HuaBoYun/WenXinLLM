<template>
  <ul v-if="theme.showThemeSetting" class="vab-theme-setting">
    <li @click="handleOpenTheme">
      <a>
        <vab-icon icon="brush-2-line" />
        <p>{{ translateTitle('主题配置') }}</p>
      </a>
    </li>
    <li @click="randomTheme">
      <a>
        <vab-icon icon="apps-line" />
        <p>{{ translateTitle('随机换肤') }}</p>
      </a>
    </li>
    <li @click="buy">
      <a>
        <vab-icon icon="shopping-cart-2-line" />
        <p>{{ translateTitle('购买源码') }}</p>
      </a>
    </li>
    <li @click="getCode">
      <a>
        <vab-icon icon="file-copy-line" />
        <p>{{ translateTitle('拷贝源码') }}</p>
      </a>
    </li>
    <li @click="removeLocalStorage">
      <a>
        <vab-icon icon="delete-bin-4-line" />
        <p>
          {{ translateTitle('清理缓存') }}
        </p>
      </a>
    </li>
  </ul>
</template>

<script>
  import { translateTitle } from '@/utils/i18n'
  import { mapGetters } from 'vuex'

  export default {
    name: 'VabThemeSetting',
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    methods: {
      translateTitle,
      handleOpenTheme() {
        this.$baseEventBus.$emit('theme')
      },
      randomTheme() {
        this.$baseEventBus.$emit('random-theme')
      },
      buy() {
        window.open('http://vue-admin-beautiful.com/authorization')
      },
      getCode() {
        this.$prompt(
          '请输入秘钥(秘钥请在源码中查看，跳转后需登录购买时绑定的github账号)',
          '温馨提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
          }
        )
          .then(({ value }) => {
            if (value !== 'vabp') {
              // 留给没有购买VueAdminBeautifulPro框架的人的赠言：既然你已经找到这里了，那说明你的能力很出众，也应该很喜欢这个框架吧，但是不绑定github账号依旧看不到源码的，所以加我qq买一个吧
              this.$baseMessage(
                '秘钥不正确！',
                'error',
                'vab-hey-message-error'
              )
            }
            let path = this.$route.path + '/index.vue'
            const _path = this.$route.path
            switch (_path) {
              case '/workbench':
                path = '/index/workbench.vue'
                break
              case '/vab/icon/remixIcon':
                path = '/vab/icon/remixIcon.vue'
                break
              case '/vab/icon/iconSelector':
                path = '/vab/icon/iconSelector.vue'
                break
              case '/vab/table/comprehensiveTable':
                path = '/vab/table/comprehensiveTable.vue'
                break
              case '/vab/table/inlineEditTable':
                path = '/vab/table/inlineEditTable.vue'
                break
              case '/vab/table/customTable':
                path = '/vab/table/customTable.vue'
                break
              case '/vab/form/comprehensiveForm':
                path = '/vab/form/comprehensiveForm.vue'
                break
              case '/vab/form/stepForm':
                path = '/vab/form/stepForm.vue'
                break
              case '/other/dynamicSegment/test1/1':
                path = '/other/dynamicSegment/test1.vue'
                break
              case '/other/dynamicSegment/test2?id=1':
                path = '/other/dynamicSegment/test2.vue'
                break
              case '/other/drag/dialogDrag':
                path = '/other/drag/dialogDrag.vue'
                break
              case '/other/drag/cardDrag':
                path = '/other/drag/cardDrag.vue'
                break
              case '/other/drag/flowSheetDrag':
                path = '/other/drag/flowSheetDrag.vue'
                break
              case '/vab/editor/richTextEditor':
                path = '/vab/editor/richTextEditor.vue'
                break
              case '/vab/editor/markdownEditor':
                path = '/vab/editor/markdownEditor.vue'
                break
              case '/other/menu1/menu1-1/menu1-1-1/menu1-1-1-1':
                path =
                  '/other/nested/menu1/menu1-1/menu1-1-1/menu1-1-1-1/index.vue'
                break
              case '/other/excel/exportExcel':
                path = '/other/excel/exportExcel.vue'
                break
              case '/other/excel/exportSelectedExcel':
                path = '/other/excel/exportSelectedExcel.vue'
                break
              case '/other/excel/exportMergeHeaderExcel':
                path = '/other/excel/exportMergeHeaderExcel.vue'
                break
            }
            window.open(
              `https://github.com/vue-admin-beautiful/admin-pro/blob/master/src/views${path}`
            )
          })
          .catch(() => {})
      },
      removeLocalStorage() {
        localStorage.clear()
        location.reload()
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-theme-setting {
    position: fixed;
    top: 50%;
    right: 0;
    z-index: $base-z-index + 1;
    padding: 10px 0 0 0;
    margin: 0;
    text-align: center;
    cursor: pointer;
    background: $base-color-white;
    border: 1px solid $base-border-color;
    border-top-left-radius: $base-border-radius + 3;
    border-bottom-left-radius: $base-border-radius + 3;
    box-shadow: 0 0 50px 0 rgb(82 63 105 / 15%);
    transform: translateY(-50%);

    > li {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 8px 10px 10px;
      margin: 0;
      list-style: none;

      &:nth-child(2) {
        [class*='ri-'] {
          animation: rotate 6s linear infinite;
        }
      }

      $colors: (
        1: #3698fd,
        2: #1bc3bb,
        3: #faa500,
        4: #b37feb,
        5: #ef4c5d,
      );

      @each $key, $color in $colors {
        &:nth-child(#{$key}) {
          a {
            color: $color;
            background: mix($base-color-white, $color, 90%);
            transition: color 0.15s ease, background-color 0.15s ease,
              border-color 0.15s ease, box-shadow 0.15s ease,
              -webkit-box-shadow 0.15s ease;

            &:hover {
              color: $base-color-white;
              background: $color;
            }
          }
        }
      }

      a {
        display: inline-block;
        width: 60px;
        height: 60px;
        padding-top: 10px;
        text-align: center;
        background: #f6f8f9;
        border-radius: $base-border-radius + 3;

        p {
          padding: 0;
          margin: 0;
          overflow: hidden;
          font-size: $base-font-size-small;
          line-height: 25px;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
</style>
