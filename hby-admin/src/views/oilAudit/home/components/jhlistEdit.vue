<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-col :span="24">
        <el-divider>
          一、专项审计{{ tableData1.length + tableData2.length }}项
        </el-divider>
        <div class="table-title">
          <span>（一）生产经营管理专项审计{{ tableData1.length }}项</span>
          <el-button
            type="success"
            @click="handleAdd('tableData1', 'table1')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm1"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm1.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData1"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch1"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData1">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLxjyzypgEdit(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="被审计单位" prop="unitRange" />
          <el-table-column
            align="center"
            label="审计范围"
            prop="auditScope"
            #default="{ row }"
          >
            {{ row.timeRangel + ' ~ ' + row.timeRangeR }}
          </el-table-column>
          <el-table-column align="center" label="备注" prop="remark" />
          <el-table-column align="center" label="项目类型" prop="itemType" />

          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData1', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData1', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm1.pageNumber"
          :layout="layout"
          :page-size="queryForm1.pageSize"
          :total="total1"
          @current-change="handleCurrentChange1"
          @size-change="handleSizeChange1"
        />
        <div class="table-title">
          <span>（二）基建与投资专项审计{{ tableData2.length }}项</span>
          <el-button
            type="success"
            @click="handleAdd('tableData2', 'table1')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm2"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm2.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData2"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch2"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData2">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLxjyzypgEdit(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="被审计单位" prop="unitRange" />
          <el-table-column
            align="center"
            label="审计范围"
            prop="auditScope"
            #default="{ row }"
          >
            {{ row.timeRangel + ' ~ ' + row.timeRangeR }}
          </el-table-column>
          <el-table-column align="center" label="备注" prop="remark" />
          <el-table-column align="center" label="项目类型" prop="itemType" />

          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData2', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData2', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm2.pageNumber"
          :layout="layout"
          :page-size="queryForm2.pageSize"
          :total="total2"
          @current-change="handleCurrentChange2"
          @size-change="handleSizeChange2"
        />
      </el-col>
      <el-col :span="24" style="margin-top: 20px">
        <el-divider>
          二、经济责任审计{{
            tableData3.length + tableData4.length + tableData5.length
          }}项
        </el-divider>
        <div class="table-title">
          <span>
            （一）二级单位及所属成员单位离任经济责任审计{{
              tableData3.length
            }}项
          </span>
          <el-button
            type="success"
            @click="handleAdd('tableData3', 'table21')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm3"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm3.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData3"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch3"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData3">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLRJYJLRSJview(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="auditOrgId"
            label="被审计单位"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.auditOrg.orgname }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="审计范围" prop="auditScope">
            <template #default="{ row }">
              {{ row.auditStartTime }} - {{ row.auditEndTime }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="委托时间" prop="entrustTime" />
          <el-table-column align="center" label="委托书编号" prop="entrustNo" />
          <el-table-column align="center" label="备注" prop="remarks" />
          <el-table-column align="center" label="项目类型" prop="projectType" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData3', row, $index, 'table21')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData3', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm3.pageNumber"
          :layout="layout"
          :page-size="queryForm3.pageSize"
          :total="total3"
          @current-change="handleCurrentChange3"
          @size-change="handleSizeChange3"
        />
        <div class="table-title">
          <span>（二）二级单位任中经济责任审计{{ tableData4.length }}项</span>
          <el-button
            type="success"
            @click="handleAdd('tableData4', 'table22')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm4"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm4.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData4"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch4"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData4">
          <el-table-column align="center" label="序号" type="index" />
          <!-- <el-table-column align="center" label="编号" prop="tbname" >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleDetail(row)"
                style="white-space: pre-line; line-height: 16px"
              >
                {{ row.tbname }}
              </el-button>
            </template>
          </el-table-column> -->
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleDetailRzsjmxView(row)"
                style="white-space: pre-line; line-height: 16px"
              >
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="orgId"
            label="被审计单位"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.org && row.org.orgname }}
            </template>
          </el-table-column>
          <el-table-column label="审计范围" prop="workStartTime" width="220">
            <template #default="{ row }">
              {{ row.workStartTime }} - {{ row.workEndTime }}
            </template>
          </el-table-column>
          <el-table-column
            label="委托时间"
            prop="entrustTime"
          ></el-table-column>
          <el-table-column label="备注" prop="remarks"></el-table-column>
          <el-table-column label="项目类型" prop="projectlx"></el-table-column>
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData4', row, $index, 'table22')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData4', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm4.pageNumber"
          :layout="layout"
          :page-size="queryForm4.pageSize"
          :total="total4"
          @current-change="handleCurrentChange4"
          @size-change="handleSizeChange4"
        />
        <div class="table-title">
          <span>（三）三级单位离任经济责任审计{{ tableData5.length }}项</span>
          <el-button
            type="success"
            @click="handleAdd('tableData5', 'table23')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm5"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm5.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData5"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch5"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData5">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleSJDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData5', row, $index, 'table23')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData5', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm5.pageNumber"
          :layout="layout"
          :page-size="queryForm5.pageSize"
          :total="total5"
          @current-change="handleCurrentChange5"
          @size-change="handleSizeChange5"
        />
      </el-col>

      <el-col :span="24" style="margin-top: 20px">
        <el-divider>
          三、工程建设项目审计{{ tableData6.length + tableData7.length }}项
        </el-divider>
        <div class="table-title">
          <span>（一）工程建设项目结算审计{{ tableData6.length }}项</span>
          <el-button
            type="success"
            @click="handleAdd('tableData6', 'table31')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm6"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm6.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData6"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch6"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData6">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleGCJSDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            label="项目金额"
            prop="projectAmount"
          ></el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData6', row, $index, 'table31')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData6', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm6.pageNumber"
          :layout="layout"
          :page-size="queryForm6.pageSize"
          :total="total6"
          @current-change="handleCurrentChange6"
          @size-change="handleSizeChange6"
        />
        <div class="table-title">
          <span>
            （二）选择建设项目投资基本情况表内容{{ tableData7.length }}项
          </span>
          <el-button
            type="success"
            @click="handleAdd('tableData7', 'table32')"
            v-if="!formDisabled"
          >
            新增
          </el-button>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm7"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm7.projectName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData7"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch7"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData7">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleJSTZDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            label="项目金额"
            prop="projectAmount"
          ></el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <!-- <el-button
                type="text"
                @click="handleEdit('tableData7', row, $index, 'table32')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> -->
              <el-button
                type="text"
                @click="handleEditDelete('tableData7', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm7.pageNumber"
          :layout="layout"
          :page-size="queryForm7.pageSize"
          :total="total7"
          @current-change="handleCurrentChange7"
          @size-change="handleSizeChange7"
        />
      </el-col>

      <el-col :span="24" style="margin-top: 20px">
        <el-divider>四、其他审计{{ tableData8.length }}项</el-divider>
        <div class="table-title">
          <span>（一）其他审计{{ tableData8.length }}项</span>
          <div>
            <el-button
              type="success"
              @click="handleAdd('tableData8')"
              v-if="!formDisabled"
            >
              新增
            </el-button>

            <el-button
              type="success"
              @click="handleAddTable8List()"
              v-if="!formDisabled"
            >
              选择
            </el-button>
          </div>
        </div>
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm8"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm8.auditItemName"
                  clearable
                  placeholder="项目名称"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="queryData8"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch8"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table :data="tableData8">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="auditItemName"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.auditItemName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="auditOrgNameStrs"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="审计范围"
            width="120"
            prop="auditScope"
          ></el-table-column>
          <el-table-column
            label="实施类型"
            width="120"
            prop="implType"
          ></el-table-column>
          <el-table-column
            label="审计项目类型"
            width="120"
            prop="auditItemType"
          ></el-table-column>
          <el-table-column
            prop="projectInitUnitName"
            label="立项单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <el-button
                type="text"
                @click="handleEdit('tableData8', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData8', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm8.pageNumber"
          :layout="layout"
          :page-size="queryForm8.pageSize"
          :total="total8"
          @current-change="handleCurrentChange8"
          @size-change="handleSizeChange8"
        />
      </el-col>
    </el-row>

    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <Table1 ref="table1" @fetch-table="fetchTable" :source="source"></Table1>
    <Table21 ref="table21" @fetch-table="fetchTable" :source="source"></Table21>
    <Table22 ref="table22" @fetch-table="fetchTable" :source="source"></Table22>
    <Table23 ref="table23" @fetch-table="fetchTable" :source="source"></Table23>
    <Table31 ref="table31" @fetch-table="fetchTable" :source="source"></Table31>
    <Table32 ref="table32" @fetch-table="fetchTable" :source="source"></Table32>
    <JhcgTable
      ref="jhcgTable"
      @fetch-table="jhTable"
      :source="source"
    ></JhcgTable>
    <table8 ref="table8" @fetch="table8Fetch" />

    <rzsjmxView ref="rzsjmxView"></rzsjmxView>
    <LRJYJLRSJview ref="LRJYJLRSJview"></LRJYJLRSJview>
    <lxjyzypgEdit ref="lxjyzypgEdit" />
    <SJmodal ref="SJmodal"></SJmodal>
    <GCJSmodal ref="GCJSmodal"></GCJSmodal>
    <JSTZmodal ref="JSTZmodal"></JSTZmodal>

    <table8List ref="table8List" @fetch="table8ListFetch" />
  </el-dialog>
</template>

<script>
  import Vue from 'vue'

  import {
    getEvaluationList,
    getOtherAuditList,
    getLeaveAuditTwoLevel,
    getRzsjmxListDraftPlan,
    getJhGlhzList,
  } from '@/oapi/audit/plan'

  import { jhgljhDetail, jhgljhSaveOrUpdate } from '@/api/monitor/question'
  import table8List from '@/views/oilAudit/jhlx/components/table/table8List'
  import JhcgTable from '@/views/oilAudit/jhlx/components/table/jhchugTable'
  import Table1 from '@/views/oilAudit/jhlx/components/table/jhcgTable1'
  import table8 from '@/views/oilAudit/jhlx/components/table/table8'
  import Table21 from '@/views/oilAudit/jhlx/components/table/table21'
  import Table22 from '@/views/oilAudit/jhlx/components/table/table22'
  import Table23 from '@/views/oilAudit/jhlx/components/zgTable/table23'
  import Table31 from '@/views/oilAudit/jhlx/components/zgTable/table31'
  import Table32 from '@/views/oilAudit/jhlx/components/zgTable/table32'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'

  import SJmodal from '@/views/oilAudit/jhlx/components/zgModal/SJModal.vue'
  import GCJSmodal from '@/views/oilAudit/jhlx/components/zgModal/GCJSModal.vue'
  import JSTZmodal from '@/views/oilAudit/jhlx/components/zgModal/JSTZModal.vue'
  import rzsjmxView from '@/views/oilAudit/lrjjzr/components/rzsjmxView.vue'
  import LRJYJLRSJview from '@/views/oilAudit/lrjjzr/components/lrjyjlrsjView.vue'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit.vue'
  import {
    deleteGL3,
    deleteGLByIds3,
    removById,
  } from '@/api/oilAudit/jhgl/jhcg'

  export default {
    name: 'gczxpxbEdit',
    inheritAttrs: false,
    components: {
      table8,
      Table1,
      Table21,
      Table22,
      Table23,
      Table31,
      Table32,
      JhcgTable,
      rzsjmxView,
      LRJYJLRSJview,
      lxjyzypgEdit,
      DepartmentOptions,
      SJmodal,
      GCJSmodal,
      JSTZmodal,
      table8List,
    },
    props: [],
    data() {
      return {
        formData: {
          jhchugid: '',
          jhmc: '',
          jhcgNo: '',
          sjxmmc: '',
          xmnd: '',
          jhlx: '',
          cjr: '',
          cjsj: '',
          jhcgid: '',
        },
        sjlxArr: {
          11: '生产经营管理专项审计',
          12: '基建与投资专项审计',
          21: '二级单位及所属成员单位离任经济责任审计',
          22: '二级单位任中经济责任审计',
          23: '三级单位离任经济责任审计',
          31: '工程建设项目结算审计',
          32: '工程建设项目竣工决算审计',
        },
        typeArr: {
          1: '11',
          2: '12',
          3: '21',
          4: '22',
          5: '23',
          6: '31',
          7: '32',
        },
        typeOptions: [
          {
            value: '1',
            label: '预结算审计',
            one: '工程项目审计',
          },
          {
            value: '2',
            label: '竣工决算审计',
            one: '工程项目审计',
          },
          {
            value: '3',
            label: '建设期间审计',
            one: '工程项目审计',
          },
          {
            value: '4',
            label: '项目后评价审计',
            one: '工程项目审计',
          },
          {
            value: '5',
            label: '其他工程审计',
            one: '工程项目审计',
          },
          {
            value: '6',
            label: '离任审计',
            one: '经济责任审计',
          },
          {
            value: '7',
            label: '任中审计',
            one: '经济责任审计',
          },
          {
            value: '8',
            label: '财务收支审计',
            one: '管理及专项审计',
          },
          {
            value: '9',
            label: '内部控制审计',
            one: '管理及专项审计',
          },
          {
            value: '10',
            label: '绩效审计',
            one: '管理及专项审计',
          },
          {
            value: '11',
            label: '信息系统审计',
            one: '管理及专项审计',
          },
          {
            value: '12',
            label: '风险管理审计',
            one: '管理及专项审计',
          },
          {
            value: '13',
            label: '联合账簿审计',
            one: '管理及专项审计',
          },
          {
            value: '14',
            label: '物资采购审计',
            one: '管理及专项审计',
          },
          {
            value: '15',
            label: '招投标审计',
            one: '管理及专项审计',
          },
          {
            value: '16',
            label: '经济合同审计',
            one: '管理及专项审计',
          },
          {
            value: '17',
            label: '联合账簿审计',
            one: '管理及专项审计',
          },
          {
            value: '18',
            label: '科技管理审计',
            one: '管理及专项审计',
          },
          {
            value: '19',
            label: '重大政策跟踪审计',
            one: '管理及专项审计',
          },
          {
            value: '20',
            label: '金融业务审计',
            one: '管理及专项审计',
          },
          {
            value: '21',
            label: '其他审计',
            one: '管理及专项审计',
          },
        ],
        formDisabled: true,
        tableData1: [],
        tableData12: [],
        tableData2: [],
        tableData22: [],
        tableData3: [],
        tableData32: [],
        tableData4: [],
        tableData42: [],
        tableData5: [],
        tableData52: [],
        tableData6: [],
        tableData62: [],
        tableData7: [],
        tableData72: [],
        tableData8: [],
        tableData82: [],
        rules: {
          jhcgNo: [
            {
              required: true,
              message: '请输入编号',
              trigger: 'blur',
            },
          ],
          jhmc: [
            {
              required: true,
              message: '请输入计划名称',
              trigger: 'blur',
            },
          ],
          sj: [
            {
              required: true,
              message: '请输入时间',
              trigger: 'blur',
            },
          ],
          lxdwmc: [
            {
              required: true,
              message: '请选择立项单位',
              trigger: 'blur',
            },
          ],
          bsjdwmc: [
            {
              required: true,
              message: '请选择被审计单位',
              trigger: 'blur',
            },
          ],
          sslx: [
            {
              required: true,
              message: '请选择实施类型',
              trigger: 'blur',
            },
          ],
          jwxm: [
            {
              required: true,
              message: '请选择境外项目',
              trigger: 'blur',
            },
          ],
          sssjjgmc: [
            {
              required: true,
              message: '请输入实施审计机构',
              trigger: 'blur',
            },
          ],
          xmnd: [
            {
              required: true,
              message: '请选择项目年度',
              trigger: 'blur',
            },
          ],
          xmfzcksmc: [
            {
              required: true,
              message: '请输入项目负责处（科）室',
              trigger: 'blur',
            },
          ],
          jhssyf: [
            {
              required: true,
              message: '请选择计划实施月份',
              trigger: 'blur',
            },
          ],
          ssxmlx: [
            {
              required: true,
              message: '请选择审计项目类型',
              trigger: 'blur',
            },
          ],
          lxyj: [
            {
              required: true,
              message: '请选择立项依据',
              trigger: 'blur',
            },
          ],
          sjxmmc: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          jhlx: [
            {
              required: true,
              message: '请选择计划类型',
              trigger: 'blur',
            },
          ],
          jhtrrr: [
            {
              required: true,
              message: '请输入计划投入人日',
              trigger: 'blur',
            },
          ],
          sfdqbjyhdsj: [
            {
              required: true,
              message: '请选择是否对全部经营活动审计',
              trigger: 'blur',
            },
          ],
          bgyy: [
            {
              required: true,
              message: '请输入变更原因',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        source: null, //表格来源
        operate: 'add',
        deptType: '',
        layout: 'total, sizes, prev, pager, next, jumper',

        queryForm1: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total1: 0,

        queryForm2: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total2: 0,
        queryForm3: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total3: 0,
        queryForm4: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total4: 0,
        queryForm5: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total5: 0,
        queryForm6: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total6: 0,
        queryForm7: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        total7: 0,
        queryForm8: {
          pageNumber: 1,
          pageSize: 20,
          auditItemName: '',
        },
        total8: 0,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSJDetail(row) {
        this.$refs['SJmodal'].showEdit(row)
      },
      handleGCJSDetail(row) {
        this.$refs['GCJSmodal'].showEdit(row)
      },
      handleJSTZDetail(row) {
        this.$refs['JSTZmodal'].showEdit(row)
      },
      table8Fetch(row) {
        this.tableData8.push(row)
      },
      chooseJhmc() {
        this.$refs.jhcgTable.showEdit()
      },
      //二.2
      handleDetailRzsjmxView(row) {
        this.$refs['rzsjmxView'].showEdit({ id: row.id }, '详情')
      },
      // 二.1
      handleDetailLRJYJLRSJview(row) {
        this.$refs['LRJYJLRSJview'].showEdit(row, true)
      },
      //一.1&2
      handleDetailLxjyzypgEdit(row) {
        this.$refs['lxjyzypgEdit'].showEdit('detail', row)
      },
      jhTable(val) {
        if (!val) return
        Object.assign(this.formData, val)
        this.formData.jhmc = val.sjxmmc
        this.tableData1 = val.mx11 || []
        this.tableData2 = val.mx12 || []
        this.tableData3 = val.leaveAudit2LEntityList || []
        this.tableData4 = val.auditSuggestion2LEntityList || []

        this.tableData5 =
          val.leaveAudit3LEntityList.map((item) => {
            const { id, jhcgid, cjsj, gxr, gxsj, ...other } = item
            return {
              ...other,
              id: '',
              relaid: id,
            }
          }) || []

        this.tableData6 =
          val.tblYqnsJsxmJbqkList.map((item) => {
            const { id, jhcgid, cjsj, gxr, gxsj, ...other } = item
            return {
              ...other,
              id: '',
              relaid: id,
            }
          }) || []

        this.tableData7 =
          val.tblYqnsGcxmjgYsjhList.map((item) => {
            const { id, jhcgid, cjsj, gxr, gxsj, ...other } = item
            return {
              ...other,
              id: '',
              relaid: id,
            }
          }) || []

        this.tableData8 = val.oauList || []
      },
      chooseUnit(type) {
        this.deptType = type
        this.$refs.audiTree.showEdit()
      },
      // 选择单位
      getDepartmentInfo(node) {
        console.log(node)
        this.$set(this.formData, this.deptType, node.name)
        if (this.deptType == 'lxdwmc') {
          this.$set(this.formData, 'lxdwid', node.id)
        } else if (this.deptType == 'bsjdwmc') {
          this.$set(this.formData, 'bsjdwid', node.id)
        } else if (this.deptType == 'sssjjgmc') {
          this.$set(this.formData, 'sssjjgid', node.id)
        }
      },
      handleAddTable8List() {
        this.$refs.table8List.showEdit(
          this.formData.jhchugid,
          2,
          this.tableData8
        )
      },
      table8ListFetch(data) {
        let tableData8 = this.tableData8
        tableData8 = tableData8.concat(data)
        console.log('tab8:', tableData8)
        this.tableData8 = tableData8
      },
      chooseDept() {
        this.$refs.department.show()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `xmfzcksmc`, node.name)
        this.$set(this.formData, `xmfzcksid`, node.id)
      },
      issjlx(row, col, val) {
        return this.sjlxArr[val]
      },
      issjlx(row, col, val) {
        return this.sjlxArr[val]
      },
      resetQueryForm1() {
        this.queryForm1 = this.$options.data().queryForm1
      },
      resetSearch1() {
        this.resetQueryForm1()
        this.fetchData1()
      },
      handleSizeChange1(val) {
        this.queryForm1.pageSize = val
        this.fetchData1()
      },
      handleCurrentChange1(val) {
        this.queryForm1.pageNumber = val
        this.fetchData1()
      },
      queryData1() {
        this.queryForm1.pageNumber = 1
        this.fetchData1()
      },
      async fetchData1() {
        const res11 = await getEvaluationList({
          xmnd: this.row.xmnd,
          projectType: '11',
          ...this.queryForm1,
        })
        this.tableData1 = res11.data.evaPage.tlist || []
        this.total1 = res11.data.evaPage.totalRecord || 0
      },
      //
      resetQueryForm2() {
        this.queryForm2 = this.$options.data().queryForm2
      },
      resetSearch2() {
        this.resetQueryForm2()
        this.fetchData2()
      },
      handleSizeChange2(val) {
        this.queryForm2.pageSize = val
        this.fetchData2()
      },
      handleCurrentChange2(val) {
        this.queryForm2.pageNumber = val
        this.fetchData2()
      },
      queryData2() {
        this.queryForm2.pageNumber = 1
        this.fetchData2()
      },
      async fetchData2() {
        const res12 = await getEvaluationList({
          xmnd: this.row.xmnd,
          projectType: '12',
          ...this.queryForm2,
        })
        this.tableData2 = res12.data.evaPage.tlist || []
        this.total2 = res12.data.evaPage.totalRecord || 0
      },
      //
      resetQueryForm3() {
        this.queryForm3 = this.$options.data().queryForm3
      },
      resetSearch3() {
        this.resetQueryForm3()
        this.fetchData3()
      },
      handleSizeChange3(val) {
        this.queryForm3.pageSize = val
        this.fetchData3()
      },
      handleCurrentChange3(val) {
        this.queryForm3.pageNumber = val
        this.fetchData3()
      },
      queryData3() {
        this.queryForm3.pageNumber = 1
        this.fetchData3()
      },
      async fetchData3() {
        const res21 = await getLeaveAuditTwoLevel({
          xmnd: this.row.xmnd,
          projectType: '21',
          ...this.queryForm3,
        })
        this.tableData3 = res21.data.laTwoPage.tlist || []
        this.total3 = res21.data.laTwoPage.totalRecord || 0
      },
      //
      resetQueryForm4() {
        this.queryForm4 = this.$options.data().queryForm4
      },
      resetSearch4() {
        this.resetQueryForm4()
        this.fetchData4()
      },
      handleSizeChange4(val) {
        this.queryForm4.pageSize = val
        this.fetchData4()
      },
      handleCurrentChange4(val) {
        this.queryForm4.pageNumber = val
        this.fetchData4()
      },
      queryData4() {
        this.queryForm4.pageNumber = 1
        this.fetchData4()
      },
      async fetchData4() {
        const res22 = await getRzsjmxListDraftPlan({
          xmnd: this.row.xmnd,
          projectType: '22',
          ...this.queryForm4,
        })
        this.tableData4 = res22.data.rzList.tlist || []
        this.total4 = res22.data.rzList.totalRecord || 0
      },
      //
      resetQueryForm5() {
        this.queryForm5 = this.$options.data().queryForm5
      },
      resetSearch5() {
        this.resetQueryForm5()
        this.fetchData5()
      },
      handleSizeChange5(val) {
        this.queryForm5.pageSize = val
        this.fetchData5()
      },
      handleCurrentChange5(val) {
        this.queryForm5.pageNumber = val
        this.fetchData5()
      },
      queryData5() {
        this.queryForm5.pageNumber = 1
        this.fetchData5()
      },
      async fetchData5() {
        const res23 = await getJhGlhzList({
          xmnd: this.row.xmnd,
          glType: '23',
          ...this.queryForm5,
        })
        this.tableData5 = res23.data.hzList.tlist || []
        this.total5 = res23.data.hzList.totalRecord || 0
      },
      resetQueryForm6() {
        this.queryForm6 = this.$options.data().queryForm6
      },
      resetSearch6() {
        this.resetQueryForm6()
        this.fetchData6()
      },
      handleSizeChange6(val) {
        this.queryForm6.pageSize = val
        this.fetchData6()
      },
      handleCurrentChange6(val) {
        this.queryForm6.pageNumber = val
        this.fetchData6()
      },
      queryData6() {
        this.queryForm6.pageNumber = 1
        this.fetchData6()
      },
      async fetchData6() {
        const res31 = await getJhGlhzList({
          xmnd: this.row.xmnd,
          glType: '31',
          ...this.queryForm6,
        })
        this.tableData6 = res31.data.hzList.tlist || []
        this.total6 = res31.data.hzList.totalRecord || 0
      },
      resetQueryForm7() {
        this.queryForm7 = this.$options.data().queryForm7
      },
      resetSearch7() {
        this.resetQueryForm7()
        this.fetchData7()
      },
      handleSizeChange7(val) {
        this.queryForm7.pageSize = val
        this.fetchData7()
      },
      handleCurrentChange7(val) {
        this.queryForm7.pageNumber = val
        this.fetchData7()
      },
      queryData7() {
        this.queryForm7.pageNumber = 1
        this.fetchData7()
      },
      async fetchData7() {
        const res32 = await getJhGlhzList({
          xmnd: this.row.xmnd,
          glType: '32',
          ...this.queryForm7,
        })
        this.tableData7 = res32.data.hzList.tlist || []
        this.total7 = res32.data.hzList.totalRecord || 0
      },
      //
      resetQueryForm8() {
        this.queryForm8 = this.$options.data().queryForm8
      },
      resetSearch8() {
        this.resetQueryForm8()
        this.fetchData8()
      },
      handleSizeChange8(val) {
        this.queryForm8.pageSize = val
        this.fetchData8()
      },
      handleCurrentChange8(val) {
        this.queryForm8.pageNumber = val
        this.fetchData8()
      },
      queryData8() {
        this.queryForm8.pageNumber = 1
        this.fetchData8()
      },
      async fetchData8() {
        const res32 = await getOtherAuditList({
          xmnd: this.row.xmnd,
          projectType: '41',
          ...this.queryForm8,
        })
        this.tableData8 = res32.data.oauList.tlist || []
        this.total8 = res32.data.oauList.totalRecord || 0
      },
      async showEdit(row, disabled) {
        console.log(row)
        this.dialogFormVisible = true
        this.formDisabled = disabled
        this.row = row
        this.resetQueryForm1()
        this.resetQueryForm2()
        this.resetQueryForm3()
        this.resetQueryForm4()
        this.resetQueryForm5()
        this.resetQueryForm6()
        this.resetQueryForm7()
        this.resetQueryForm8()
        if (row) {
          const res11 = await getEvaluationList({
            xmnd: row.xmnd,
            projectType: '11',
            projectName: '',
          })
          this.tableData1 = res11.data.evaPage.tlist || []
          this.total1 = res11.data.evaPage.totalRecord || 0

          const res12 = await getEvaluationList({
            xmnd: row.xmnd,
            projectType: '12',
            projectName: '',
          })
          this.tableData2 = res12.data.evaPage.tlist || []
          this.total2 = res12.data.evaPage.totalRecord || 0

          const res21 = await getLeaveAuditTwoLevel({
            xmnd: row.xmnd,
            projectType: '21',
            projectName: '',
          })
          this.tableData3 = res21.data.laTwoPage.tlist || []
          this.total3 = res21.data.laTwoPage.totalRecord || 0

          const res22 = await getRzsjmxListDraftPlan({
            xmnd: row.xmnd,
            projectType: '22',
            projectName: '',
          })
          this.tableData4 = res22.data.rzList.tlist || []
          this.total4 = res22.data.rzList.totalRecord || 0

          const res23 = await getJhGlhzList({
            xmnd: row.xmnd,
            glType: '23',
            projectName: '',
          })
          this.tableData5 = res23.data.hzList.tlist || []
          this.total5 = res23.data.hzList.totalRecord || 0

          const res31 = await getJhGlhzList({
            xmnd: row.xmnd,
            glType: '31',
            projectName: '',
          })
          this.tableData6 = res31.data.hzList.tlist || []
          this.total6 = res31.data.hzList.totalRecord || 0

          const res32 = await getJhGlhzList({
            xmnd: row.xmnd,
            glType: '32',
            projectName: '',
          })
          this.tableData7 = res32.data.hzList.tlist || []
          this.total7 = res32.data.hzList.totalRecord || 0

          const res41 = await getOtherAuditList({
            xmnd: row.xmnd,
            auditItemName: '',
          })
          this.tableData8 = res41.data.oauList.tlist || []
          this.total8 = res41.data.oauList.totalRecord || 0
          // const res = await jhgljhDetail({ jhid: row.jhid })
          // Object.assign(this.formData, res.data)
          // this.tableData1 = res.data.mx11 || []
          // this.tableData12 = JSON.parse(JSON.stringify(res.data.mx11 || []))
          // this.tableData2 = res.data.mx12 || []
          // this.tableData22 = JSON.parse(JSON.stringify(res.data.mx12 || []))
          // this.tableData3 = res.data.leaveAudit2LEntityList || []
          // this.tableData32 = JSON.parse(
          //   JSON.stringify(res.data.leaveAudit2LEntityList || [])
          // )
          // this.tableData4 = res.data.auditSuggestion2LEntityList || []
          // this.tableData42 = JSON.parse(
          //   JSON.stringify(res.data.auditSuggestion2LEntityList || [])
          // )

          // this.tableData5 = res.data.leaveAudit3LEntityList || []
          // this.tableData52 = JSON.parse(
          //   JSON.stringify(res.data.leaveAudit3LEntityList || [])
          // )
          // this.tableData6 = res.data.tblYqnsJsxmJbqkList || []
          // this.tableData62 = JSON.parse(
          //   JSON.stringify(res.data.tblYqnsJsxmJbqkList || [])
          // )
          // this.tableData7 = res.data.tblYqnsGcxmjgYsjhList || []
          // this.tableData72 = JSON.parse(
          //   JSON.stringify(res.data.tblYqnsGcxmjgYsjhList || [])
          // )
          // this.tableData8 = res.data.oauList || []
          // this.tableData82 = JSON.parse(JSON.stringify(res.data.oauList || []))
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = new Date().toJSON().split('T')[0]
          return
        }
        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }
      },
      close() {
        this.formData = {
          tblYqnsJhglJhchugGLList: undefined,
          xmmc: undefined,
          jhmc: undefined,
          sj: undefined,
          lxdwmc: undefined,
          bsjdwmc: undefined,
          sslx: undefined,
          jwxm: undefined,
          sssjjgmc: undefined,
          xmnd: undefined,
          xmfzcksmc: undefined,
          jhssyf: undefined,
          ssxmlx: undefined,
          lxyj: undefined,
          sjxmmc: undefined,
          jhlx: undefined,
          jhtrrr: undefined,
          sfdqbjyhdsj: undefined,
          bgyy: undefined,
          cjr: undefined,
          cjsj: undefined,
        }
        this.dialogFormVisible = false
        this.tableData1 = []
        this.tableData2 = []
        this.tableData3 = []
        this.tableData4 = []
        this.tableData5 = []
        this.tableData6 = []
        this.tableData7 = []
        this.formDisabled = true
        this.title = '新增'
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = []
            const tableData1 = this.tableData1.filter((item) =>
              this.tableData12.every((subItem) => subItem.id !== item.id)
            )
            tableData1.map((item) => {
              attids.push({ glId: item.id, gltype: '11', relaid: item.relaid })
            })

            const tableData2 = this.tableData2.filter((item) =>
              this.tableData22.every((subItem) => subItem.id !== item.id)
            )
            tableData2.map((item) => {
              attids.push({ glId: item.id, gltype: '12', relaid: item.relaid })
            })
            const tableData3 = this.tableData3.filter((item) =>
              this.tableData32.every((subItem) => subItem.id !== item.id)
            )
            tableData3.map((item) => {
              attids.push({ glId: item.id, gltype: '21', relaid: item.relaid })
            })
            const tableData4 = this.tableData4.filter((item) =>
              this.tableData42.every((subItem) => subItem.id !== item.id)
            )
            tableData4.map((item) => {
              attids.push({ glId: item.id, gltype: '22', relaid: item.relaid })
            })

            const tableData5 = this.tableData5.filter((item) =>
              this.tableData52.every(
                (subItem) => subItem.relaOrgName !== item.relaOrgName
              )
            )
            tableData5.map((item) => {
              attids.push({ ...item, gltype: '23' })
            })
            const tableData6 = this.tableData6.filter((item) =>
              this.tableData62.every(
                (subItem) => subItem.relaOrgName !== item.relaOrgName
              )
            )
            tableData6.map((item) => {
              attids.push({ ...item, gltype: '31' })
            })
            const tableData7 = this.tableData7.filter((item) =>
              this.tableData72.every(
                (subItem) => subItem.relaOrgName !== item.relaOrgName
              )
            )
            tableData7.map((item) => {
              attids.push({ ...item, gltype: '32' })
            })

            const tableData8 = this.tableData8.filter((item) =>
              this.tableData82.every(
                (subItem) => subItem.auditId !== item.auditId
              )
            )
            let otherAuditIdsStrs = tableData8
              .map((item) => {
                return item.auditId
              })
              .join(',')

            let params = {
              ...this.formData,
              tblYqnsJhglJhGLList: attids,
              otherAuditIdsStrs,
            }
            console.log('zz', params)
            Vue.delete(params, 'mx11')
            Vue.delete(params, 'mx12')
            Vue.delete(params, 'mx13')
            Vue.delete(params, 'leaveAudit2LEntityList')
            Vue.delete(params, 'mx22')
            Vue.delete(params, 'leaveAudit3LEntityList')
            Vue.delete(params, 'tblYqnsJsxmJbqkList')
            Vue.delete(params, 'tblYqnsGcxmjgYsjhList')
            if (this.title == '新增') {
              const data = await jhgljhSaveOrUpdate({
                ...params,
              })
              if (data.code == 1) {
                this.$baseMessage(data.msg, 'success')
                this.$emit('fetch-data')
                this.close()
              }
            } else {
              const data = await jhgljhSaveOrUpdate({
                ...params,
              })
              if (data.code == 1) {
                this.$baseMessage(data.msg, 'success')
                this.$emit('fetch-data')
                this.close()
              }
            }
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handleDetail(row) {
        this.$refs['table8'].showEdit('detail', row)
      },
      handleAdd(source, ref) {
        this.source = source
        const projectType = source === 'tableData1' ? '财务' : '工程'
        this.operate = 'add'
        this.currentIndex = ''
        if (source == 'tableData8') {
          this.$refs['table8'].showEdit('add', null)
        } else if (
          source == 'tableData1' ||
          source == 'tableData2' ||
          source == 'tableData3' ||
          source == 'tableData4'
        ) {
          this.$refs[ref].showEdit('add', projectType, 3)
        } else if (source == 'tableData5') {
          this.$refs[ref].showEdit(this.formData.jhchugid, this.tableData5)
        } else if (source == 'tableData6') {
          this.$refs[ref].showEdit(this.formData.jhchugid, this.tableData6)
        } else if (source == 'tableData7') {
          this.$refs[ref].showEdit(this.formData.jhchugid, this.tableData7)
        } else {
          if (ref) {
            this.$refs[ref].showEdit('add', projectType, 3)
          } else {
            this.projectType = projectType
            this.$refs['table1'].showEdit()
          }
        }
      },
      handleEdit(source, data, index, ref) {
        console.log(source, data, index)
        this.source = source
        this.operate = 'edit'
        this.currentIndex = index
        if (source === 'tableData1' || source === 'tableData2') {
          this.auditScopeFormData.auditScope = data.auditScope
          this.appendVisible = true
          return
        } else if (source === 'tableData3') {
          this.auditScopeFormData2.entrustTime = data.entrustTime
          this.auditScopeFormData2.projectType = data.projectType
          this.auditScopeFormData2.auditScope = data.auditScope
          this.auditScopeFormData2.remarks = data.remarks
          this.appendVisible2 = true
          return
        } else if (source === 'tableData4') {
          this.auditScopeFormData3.entrustTime = data.entrustTime
          this.auditScopeFormData3.projectType = data.projectType
          this.auditScopeFormData3.auditScope = data.auditScope
          this.auditScopeFormData3.remarks = data.remarks
          this.appendVisible3 = true
          return
        } else if (source === 'tableData5') {
          this.auditScopeFormData4.unitCount = data.unitCount
          this.auditScopeFormData4.remarks = data.remarks
          this.appendVisible4 = true
          return
        } else if (source === 'tableData6') {
          this.auditScopeFormData5.itemCount = data.itemCount
          this.appendVisible5 = true
          return
        } else if (source === 'tableData7') {
          this.auditScopeFormData6.itemCount = data.itemCount
          this.appendVisible6 = true
          return
        } else if (source === 'tableData8') {
          this.$refs['table8'].showEdit('edit', data)
          return
        }
        if (ref) {
          this.$refs[ref].showEdit(data)
        }
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      fetchTable(val) {
        console.log(val)
        if (this.source !== 'tableData1' && this.source !== 'tableData2') {
          val = val[0]
        }
        if (this.operate === 'add') {
          if (
            this.source == 'tableData5' ||
            this.source == 'tableData6' ||
            this.source == 'tableData7'
          ) {
            this[this.source].push({ ...val, id: '', relaid: val.id })
          } else {
            this[this.source].push(val)
          }
        } else {
          const arr = this[this.source]
          arr.splice(this.currentIndex, 1, val)
          console.log(arr, this.source)
          this.$set(this, this.source, arr)
        }
      },
      async handleEditDelete(source, index) {
        if (source == 'tableData8') {
          if (this[source][index].auditId) {
            await removById({ auditId: this[source][index].auditId })
          }
        } else if (
          source == 'tableData7' ||
          source == 'tableData6' ||
          source == 'tableData5'
        ) {
          if (this[source][index].id) {
            await deleteGL3({ id: this[source][index].id })
          }
        } else {
          if (this[source][index].id) {
            await deleteGLByIds3({
              formid: this[source][index].id,
              jhid: this.formData.jhid,
            })
          }
        }
        this[source].splice(index, 1)
        this[source + '2'].splice(index, 1)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }

  .table-title {
    padding: 10px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
