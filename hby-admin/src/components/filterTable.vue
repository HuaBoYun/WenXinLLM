<template>
  <div class="page-filter-search">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>
          表格筛选
          <small class="color-gray">&nbsp;&nbsp;拖动调整位置</small>
        </span>
        <el-button
          style="float: right; padding: 3px 0; margin-left: 20px"
          type="text"
          @click="clearFilter"
        >
          重置
        </el-button>
      </div>
      <vuedraggable
        class="wrapper"
        chosenClass="chosen"
        v-model="allList"
        @change="dragChange"
      >
        <transition-group>
          <div v-for="item in allList" :key="item.name" class="item">
            <el-checkbox
              :label="item.name"
              v-model="item.show"
              @change="selectChange"
            >
              {{ item.name }}
            </el-checkbox>
          </div>
        </transition-group>
      </vuedraggable>
    </el-card>
  </div>
</template>

<script>
  import vuedraggable from 'vuedraggable'
  export default {
    name: 'filterTable',
    components: { vuedraggable },
    props: {
      list: {
        type: Array,
        default: [],
        required: true,
      },
      name: {
        type: String,
        default: '',
        required: true,
      },
    },
    data() {
      return {
        allList: [],
      }
    },
    created() {
      //阻止火狐浏览器拖拽打开新标签
      document.body.ondrop = function (event) {
        event.preventDefault()
        event.stopPropagation()
      }
      this.init()
    },
    methods: {
      init() {
        let data = localStorage.getItem(this.name)
        if (data) {
          this.allList = JSON.parse(data)
        } else {
          let tempArr = []
          for (let i = 0; i < this.list.length; i++) {
            let tempItem = {
              ...this.list[i],
              name: this.list[i].name,
              show: true,
            }
            tempArr.push(tempItem)
          }
          this.allList = tempArr
        }
      },
      clearFilter() {
        localStorage.removeItem(this.name)
        this.init()
        this.updateParentShow()
      },
      selectChange() {
        localStorage.setItem(this.name, JSON.stringify(this.allList))
        this.updateParentShow()
      },
      dragChange() {
        localStorage.setItem(this.name, JSON.stringify(this.allList))
        this.updateParentShow()
      },
      updateParentShow() {
        this.$emit('updateTableShow')
      },
    },
  }
</script>
<style>
  /*被拖拽对象的样式*/
  .page-filter-search .item {
    padding: 6px;
    background-color: #fdfdfd;
    border: solid 1px #eee;
    margin-bottom: -1px;
    cursor: move;
  }
  /*选中样式*/
  .page-filter-search .chosen {
    border: solid 2px #3089dc !important;
  }
  .page-filter-search .el-card__body {
    padding: 10px;
  }
</style>
