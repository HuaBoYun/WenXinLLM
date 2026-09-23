<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <vab-query-form>
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="请输入标题"
              suffix-icon="el-icon-search"
              style="width: 180px; margin-right: 20px"
            />
            <!-- <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="fetchData('reset')" type="primary">重置</el-button>

            </el-form-item> -->
          </el-form>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel :span="24">
          <el-row>
            <el-button>新建</el-button>

            <el-dropdown style="margin-left: 10px">
              <el-button>
                更多操作
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>批量增加</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-row>
        </vab-query-form-right-panel>

        <el-table v-loading="listLoading" :data="list" :show-header="false">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column align="center" label="标题" prop="name" sortable>
            <template #default="{ row }">
              <div class="row-content">
                <div class="title">{{ row.name }}</div>
                <div class="desc">{{ row.name1 }}</div>
                <div class="other">
                  <div class="other-item">模板：{{ row.name2 }}</div>
                  <div class="other-item">作者：{{ row.name3 }}</div>
                  <div class="other-item">发布日期：{{ row.name4 }}</div>
                  <div class="other-item">推荐：{{ row.name5 }}</div>
                  <div class="other-item">点评：{{ row.name6 }}</div>
                  <div class="other-item">浏览：{{ row.name7 }}</div>
                  <div class="other-item">评分：{{ row.name8 }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="pagination"
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </vab-query-form>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        activeName: 'first',
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 10,
        },
        total: 1,
        layout: 'total, sizes, prev, pager, next, jumper',
        listLoading: false,
        list: [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            name1:
              '企业业务不同，对应用的需求不同。单说上云，企业规模小了，只希望在云服务的基础上开展业务，灵活多变;企业规模大了，则希望在有公有云、私有云和混合云多样的部署，将云ERP等应用和线下业务相结合;而那些创新意识强，则希望在云上构建各种生态圈，重构商业模式。',
            name2: '客户案例',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            name5: '0',
            name6: '0',
            name7: '450',
            name8: '3.3',
          },
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            name1:
              '企业业务不同，对应用的需求不同。单说上云，企业规模小了，只希望在云服务的基础上开展业务，灵活多变;企业规模大了，则希望在有公有云、私有云和混合云多样的部署，将云ERP等应用和线下业务相结合;而那些创新意识强，则希望在云上构建各种生态圈，重构商业模式。',
            name2: '客户案例',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            name5: '0',
            name6: '0',
            name7: '450',
            name8: '3.3',
          },
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            name1:
              '企业业务不同，对应用的需求不同。单说上云，企业规模小了，只希望在云服务的基础上开展业务，灵活多变;企业规模大了，则希望在有公有云、私有云和混合云多样的部署，将云ERP等应用和线下业务相结合;而那些创新意识强，则希望在云上构建各种生态圈，重构商业模式。',
            name2: '客户案例',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            name5: '0',
            name6: '0',
            name7: '450',
            name8: '3.3',
          },
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            name1:
              '企业业务不同，对应用的需求不同。单说上云，企业规模小了，只希望在云服务的基础上开展业务，灵活多变;企业规模大了，则希望在有公有云、私有云和混合云多样的部署，将云ERP等应用和线下业务相结合;而那些创新意识强，则希望在云上构建各种生态圈，重构商业模式。',
            name2: '客户案例',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            name5: '0',
            name6: '0',
            name7: '450',
            name8: '3.3',
          },
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            name1:
              '企业业务不同，对应用的需求不同。单说上云，企业规模小了，只希望在云服务的基础上开展业务，灵活多变;企业规模大了，则希望在有公有云、私有云和混合云多样的部署，将云ERP等应用和线下业务相结合;而那些创新意识强，则希望在云上构建各种生态圈，重构商业模式。',
            name2: '客户案例',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            name5: '0',
            name6: '0',
            name7: '450',
            name8: '3.3',
          },
        ],
      }
    },
    methods: {
      handleClick(tab, event) {},
      filterTag(value, row) {
        return row.tag === value
      },
      handleCurrentChange() {},
      handleSizeChange() {},
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }

  .row-content {
    text-align: left !important;
  }

  .row-content .title {
    color: #333;
    font-size: 16px;
    text-align: left !important;
  }

  .row-content .desc {
    color: #999;
    margin: 8px 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding-right: 20px;
  }

  .row-content .other {
    display: flex;
    align-items: center;
    color: #999;
  }

  .row-content .other .other-item {
    margin-right: 20px;
    color: #999;
  }
</style>
