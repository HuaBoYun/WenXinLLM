<template>
  <div class="content system-log-container">
    <div class="navBox">
      <el-card shadow="never">
        <el-empty v-if="navList.length == 0" description="暂无数据"></el-empty>
        <div
          v-else
          class="sidebarWrap"
          v-for="(i, index) in navList"
          :key="index"
        >
          <div class="headerTitle">{{ i.label }}</div>
          <div class="treeWrap">
            <el-tree
              ref="tree"
              node-key="value"
              :data="i.data"
              :props="defaultProps"
              @node-click="
                (data, node, item) => handleNodeClick(data, node, item, i.label)
              "
            ></el-tree>
          </div>
        </div>
      </el-card>
    </div>
    <div class="listBox">
      <el-card shadow="never">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input v-model="queryForm.title" clearable placeholder="搜索" />
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.issueDateSort" placeholder="排序">
              <el-option label="相关性" value="" />
              <el-option label="↓ 发布日期" value="asc" />
              <el-option label="↑ 发布日期" value="desc" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card shadow="never" class="secondCard">
        <div v-if="screenList.length > 0">
          <span>检索条件：</span>
          <el-tag
            type="info"
            effect="plain"
            closable
            v-for="(item, index) in screenList"
            :key="index"
            @close="screenList.splice(index, 1)"
          >
            {{ item.key }} ： {{ item.value }}
          </el-tag>
          <el-divider direction="vertical" style="height: 20px"></el-divider>
          <el-button
            type="text"
            size="medium"
            icon="el-icon-delete"
            @click="screenList = []"
          >
            清空
          </el-button>
        </div>
        <div class="tablistHeader">
          <div class="tablistHeaderTitle">
            本次检索到
            <span>{{ queryForm.pageTotal }}</span>
            篇
          </div>
          <div class="">
            <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="isAllChecked"
              @change="handleCheckAllChange"
            >
              全部
            </el-checkbox>
            <el-button
              native-type="submit"
              size="mini"
              style="margin-left: 10px"
              @click="handleZipDownload"
            >
              下载
            </el-button>
            <el-button
              type="success"
              size="mini"
              style="margin-left: 10px"
              @click="open"
            >
              新建
            </el-button>
          </div>
        </div>
        <el-empty v-if="dataList.length == 0" description="暂无数据"></el-empty>
        <div v-else class="listData">
          <!-- <el-checkbox-group> -->
          <div
            class="listDataItem"
            v-for="(i, index) in dataList"
            :key="index"
            @click="openDetails(i)"
          >
            <div class="listItemLeft">
              <div class="itemTitle">
                <div @click.stop>
                  <el-checkbox
                    v-model="i.check"
                    @change="handleCheckedChange(i)"
                  />
                </div>
                <div class="listItemTitle">{{ i.title }}</div>
              </div>
              <div class="tagList"></div>
              <div class="itemType">
                <span>{{ i.riskLevelName }} /</span>
                <span>{{ i.interestPartyName }} /</span>
                <span>{{ i.contractTypeName }} /</span>
                <span>{{ i.industryTypeName }} /</span>
                <span>{{ i.termsTypeName }} /</span>
                <span>{{ i.issueDate }}发布</span>
              </div>
              <div class="itemFooter"></div>
            </div>
            <div class="listItemRight">
              <el-button
                type="text"
                size="medium"
                @click.stop="handleDownload(i)"
              >
                下载
              </el-button>
            </div>
          </div>
          <!-- </el-checkbox-group> -->
        </div>
      </el-card>
      <el-pagination
        background
        class="pagination"
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="queryForm.pageTotal"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <Edit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import Edit from './components/htyskDetail.vue'
  import {
    getTermsList,
    getSidebar,
    termsDownload,
    termsZipDownload,
  } from '@/api/sjzt/ht/index'
  export default {
    name: 'htysk',
    components: {
      Edit,
    },
    data() {
      return {
        navList: [],
        dataList: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        queryForm: {
          contractTypeCode: '', //合同类型
          interestPartyCode: '', //条款利益倾向方
          issueDateSort: '', //排序
          issueYear: '', //发布年份
          termsTypeCode: '', //条款类型
          title: '', //标题
          pageNumber: 1, //页码
          pageSize: 20, //每页数量
          pageTotal: 0, //总数
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        screenList: [],
        selectedItems: [],
        isAllChecked: false,
        isIndeterminate: false,
      }
    },
    watch: {
      screenList: {
        handler: function (newValue, oldValue) {
          let that = this
          that.queryForm.contractTypeCode = ''
          that.queryForm.termsTypeCode = ''
          that.queryForm.interestPartyCode = ''
          that.queryForm.issueYear = ''
          newValue.map((i) => {
            if (i.type == 'contractType') {
              that.queryForm.contractTypeCode = i.value
            } else if (i.type == 'termsType') {
              that.queryForm.termsTypeCode = i.value
            } else if (i.type == 'interestParty') {
              that.queryForm.interestPartyCode = i.value
            } else if (i.type == 'issueYear') {
              that.queryForm.issueYear = i.value
            }
          })
          that.fetchData()
        },
        deep: true,
      },
    },
    created() {
      this.fetchData()
      this.getNav()
    },
    methods: {
      async getNav() {
        let navRes = await getSidebar({
          types: 'contractType,termsType,interestParty,issueYear',
        })
        this.navList = navRes.data.data
      },
      queryData() {
        this.fetchData()
      },
      async fetchData() {
        let res = await getTermsList(this.queryForm)
        this.dataList = res.data.data.records.map((i) => {
          return { check: false, ...i }
        })
        this.queryForm.pageTotal = res.data.data.total
        this.queryForm.pageNumber = res.data.data.current || 1
        this.queryForm.pageSize = res.data.data.size
      },
      resetQueryForm() {
        this.queryForm = {
          contractTypeCode: '', //合同类型
          interestPartyCode: '', //条款利益倾向方
          issueDateSort: '', //排序
          issueYear: '', //发布年份
          termsTypeCode: '', //条款类型
          title: '', //标题
          pageNumber: 1, //页码
          pageSize: 20, //每页数量
          pageTotal: 0, //总数
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.screenList = []
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleDownFile(row) {
        this.$refs['htfbkDetail'].showEdit(row)
      },
      open() {
        this.$refs.edit.showEdit()
      },
      handleNodeClick(data, node, item, param) {
        let dataArray = this.screenList
        let newData = { key: param, value: data.label, type: data.typeValue }
        const existingItem = dataArray.find((item) => item.key === newData.key)
        if (existingItem) {
          existingItem.value = newData.value
        } else {
          dataArray.push(newData)
        }
        this.screenList = dataArray
      },
      openDetails(i) {
        this.$refs.edit.showEdit(i.id)
      },
      async handleDownload(row) {
        const res = await termsDownload({ termsId: row.id })
        this.downloadFileByBlob(res, row.title)
      },
      async handleZipDownload(row) {
        if (this.selectedItems.length == 0) {
          this.$message.error('请至少选择一条')
          return false
        }
        const res = await termsZipDownload({
          termsIds: this.selectedItems.join(','),
        })
        this.downloadFileByBlob(res, '合同要素库')
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      handleCheckAllChange(val) {
        this.checkedCities = val ? true : false
        if (val) {
          this.dataList = this.dataList.map((i) => {
            return { ...i, check: true }
          })
          this.selectedItems = this.dataList.map((i) => {
            return i.id
          })
        } else {
          this.dataList = this.dataList.map((i) => {
            return { ...i, check: false }
          })
          this.selectedItems = []
        }
        this.isIndeterminate = false
      },
      handleCheckedChange(i) {
        if (i.check) {
          this.selectedItems.push(i.id)
        } else {
          var newArray = this.selectedItems.filter(function (value) {
            return value !== i.id
          })
          this.selectedItems = newArray
        }
        let checkedCount = this.dataList.length
        this.isAllChecked = checkedCount === this.selectedItems.length
        this.isIndeterminate =
          this.selectedItems.length > 0 &&
          this.selectedItems.length < checkedCount
      },
    },
  }
</script>
<style scoped lang="scss">
  .content {
    width: 100%;

    padding: 15px;
    display: flex;

    .navBox {
      width: 245px;
      max-height: calc(100vh - 240px);
      overflow: auto;
      padding-right: 5px;

      .sidebarWrap {
        max-height: 280px;
        margin-bottom: 10px;

        .headerTitle {
          font-size: 18px;
          font-weight: 500;
          letter-spacing: 0;
          line-height: 20px;
          color: #2f2e3f;
          line-height: 30px;
          border-bottom: 1px solid #dcdfe5;
          margin: 0 10px 10px;
          position: relative;
          font-style: italic;
        }

        .headerTitle:after {
          content: '';
          height: 80%;
          width: 6px;
          background: rgb(228, 85, 56);
          border-radius: 4px;
          position: absolute;
          top: 2px;
          left: -10px;
        }
      }

      .treeWrap {
        max-height: 240px;
        overflow: auto;
        margin-bottom: 10px;
      }
    }

    .listBox {
      padding-left: 15px;
      flex: 1;
      display: flex;
      flex-direction: column;
      // height: calc(100vh - 240px);

      .tablistHeader {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid #dcdfe5;
        padding: 10px 0 0 0;
        min-height: 50px;

        .tablistHeaderTitle {
          font-size: 13px;
          font-weight: 500;
          color: #2f2e3f;

          span {
            color: rgb(228, 85, 56);
          }
        }
      }

      .listData {
        max-height: calc(100vh - 500px);
        overflow-y: scroll;

        .listDataItem {
          display: flex;
          width: 100%;
          margin-bottom: 6px;
          cursor: pointer;

          .listItemLeft {
            flex: 1;

            .itemTitle {
              font-size: 16px;
              color: #2f2e3f;
              padding: 16px 10px 6px 40px;
              position: relative;
              display: flex;

              .el-checkbox {
                position: absolute;
                left: 16px;
                top: 20px;
              }

              .listItemTitle {
                line-height: 24px;
              }
            }

            .tagList {
              padding: 0 10px 0 40px;
              margin-bottom: 5px;
            }

            .itemType {
              padding: 0 10px 5px 40px;
              display: inline-block;
              font-size: 14px;
              font-weight: 400;
              line-height: 20.27px;
              color: #909399;
            }

            .itemFooter {
              padding: 0 10px 10px 40px;
            }
          }

          .listItemRight {
            font-size: 14px;
            text-align: right;
            padding: 10px 20px 10px 10px;
          }
        }

        .listDataItem:hover {
          background-color: #f3f5f8;
        }
      }
    }
  }

  :deep(.el-tree-node__content) {
    height: 32px !important;
    font-size: 14px;
    color: #2f2e3f;
    margin-bottom: 2px;
  }

  :deep(.el-tree-node__label) {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .search {
    padding: 20px 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }

  .secondCard {
    margin-top: -5px !important;
    margin-bottom: 10px !important;
  }

  .pagination {
    margin-bottom: 20px !important;
  }
</style>
