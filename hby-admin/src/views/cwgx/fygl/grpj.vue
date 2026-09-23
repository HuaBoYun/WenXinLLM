<!-- 个人票夹 -->
<template>
  <div class="system-log-container">
    <div class="tab-container">
      <div
        v-for="(item, index) in tabs"
        :key="index"
        @click="selectTab(index)"
        :class="{ active: currentIndex === index }"
        class="tab-item"
      >
        <div>{{ item.title }}</div>
        <div :class="{ line: currentIndex === index }"></div>
      </div>
    </div>
    <template v-if="currentIndex === 0">
      <div class="container_box">
        <div
          :class="[currentIndex === 0 ? 'container_left' : 'container_left_2']"
        >
          <div class="left_tabs">
            <div
              v-for="(item, index) in childrenTabs"
              :key="index"
              @click="selectChildrenTab(index)"
              :class="{ active: childrenIndex === index }"
              class="tab-item"
            >
              <div>{{ item.title }}</div>
              <div :class="{ line: childrenIndex === index }"></div>
            </div>
          </div>
          <template v-if="childrenIndex === 0">
            <div class="left_tabs_add" @click="handleAddTicketBag">
              <span><i class="el-icon-plus"></i></span>
              <span class="piaodai">新建票袋</span>
            </div>
            <el-input
              placeholder="票袋名称"
              v-model="queryForm.ticketValue"
              clearable
              class="search_input"
            ></el-input>
          </template>
        </div>
        <template v-if="childrenIndex === 0">
          <div class="container_right">
            <vab-query-form>
              <el-card shadow="never">
                <vab-query-form-left-panel :span="24">
                  <el-form
                    ref="form"
                    :inline="true"
                    label-width="0"
                    :model="queryForm"
                    @submit.native.prevent
                  >
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="发票号码"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="发票代码"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-select
                          v-model="queryForm.jsFinance"
                          placeholder="发票类型"
                        >
                          <el-option
                            v-for="item in options_1"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-form-item>
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
                      <el-button
                        native-type="submit"
                        type="primary"
                        @click="resetSearch"
                      >
                        重置
                      </el-button>
                    </el-form-item>
                    <!-- <el-form-item>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      content="搜索筛选"
                      placement="top"
                    >
                      <el-popover placement="left" trigger="click">
                        <filter-search
                          v-if="true"
                          :list="searchAll"
                          :name="localKey"
                          @updateSearchShow="initSearch"
                        />
                        <el-button slot="reference" style="height: 32px">
                          <vab-icon icon="filter" :is-custom-svg="true" />
                        </el-button>
                      </el-popover>
                    </el-tooltip>
                  </el-form-item> -->
                    <el-form-item>
                      <span
                        :class="
                          searchMore ? 'search-more is-opened' : 'search-more'
                        "
                        @click="showMore"
                      >
                        <span>{{ searchMore ? '收起' : '展开' }}</span>
                        <i class="el-icon-arrow-down"></i>
                      </span>
                    </el-form-item>
                  </el-form>
                </vab-query-form-left-panel>
              </el-card>
            </vab-query-form>

            <el-card shadow="never" class="secondCard">
              <vab-query-form-right-panel style="width: 100%">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="表格筛选"
                  placement="top"
                >
                  <el-popover placement="right" trigger="click">
                    <filter-table
                      :list="filedAll"
                      :name="tableKey"
                      @updateTableShow="initTable"
                    />
                    <!-- <i class="el-icon-delete" slot="reference"></i> -->
                    <el-button
                      slot="reference"
                      icon="el-icon-s-grid"
                      class="biaoge"
                      style="margin-bottom: 10px; margin-right: 10px"
                    ></el-button>
                  </el-popover>
                </el-tooltip>
                <!-- <el-button type="success" @click="handleEdit(false, false)">
                  新建
                </el-button> -->
                <el-button type="primary">去报销</el-button>
              </vab-query-form-right-panel>

              <el-table v-loading="listLoading" :data="list">
                <el-table-column
                  align="center"
                  label="序号"
                  prop="qdcode"
                  width="100"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="发票类型"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="发票号码"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="发票代码"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="销售方名称"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="购买方名称"
                  prop="sjlxName"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="不含税金额"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="税额"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="价税合计"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="查验状态"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="发票报销状态"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="开票日期"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="上传时间"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="单据编号"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      @click="handleEdit(scope.row, false)"
                      :disabled="!!scope.row.spzt"
                    >
                      修改
                    </el-button>
                    <el-dropdown style="margin-left: 10px">
                      <el-button type="text">更多</el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>
                          <el-button
                            @click="handleDelete(scope.row)"
                            type="text"
                            :disabled="!!scope.row.spzt"
                          >
                            删除
                          </el-button>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>

            <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </div>
        </template>
        <template v-if="childrenIndex === 1">
          <div
            :class="[
              currentIndex === 1 ? 'container_right' : 'container_right_2',
            ]"
          >
            <vab-query-form>
              <el-card shadow="never">
                <vab-query-form-left-panel :span="24">
                  <el-form
                    ref="form"
                    :inline="true"
                    label-width="0"
                    :model="queryForm"
                    @submit.native.prevent
                  >
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="关联申请单单号"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="订单号"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-select
                          v-model="queryForm.jsFinance"
                          placeholder="出行方式"
                        >
                          <el-option
                            v-for="item in options_1"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="出发地"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="目的地"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-input
                          v-model="queryForm.jsFinance"
                          clearable
                          placeholder="实际出行人"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item>
                        <el-date-picker
                          v-model="queryForm.day"
                          type="daterange"
                          align="right"
                          unlink-panels
                          range-separator="至"
                          start-placeholder="创建时间开始日期"
                          end-placeholder="创建时间结束日期"
                        ></el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-form-item>
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
                      <el-button
                        native-type="submit"
                        type="primary"
                        @click="resetSearch"
                      >
                        重置
                      </el-button>
                    </el-form-item>
                    <!-- <el-form-item>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      content="搜索筛选"
                      placement="top"
                    >
                      <el-popover placement="left" trigger="click">
                        <filter-search
                          v-if="true"
                          :list="searchAll"
                          :name="localKey"
                          @updateSearchShow="initSearch"
                        />
                        <el-button slot="reference" style="height: 32px">
                          <vab-icon icon="filter" :is-custom-svg="true" />
                        </el-button>
                      </el-popover>
                    </el-tooltip>
                  </el-form-item> -->
                    <el-form-item>
                      <span
                        :class="
                          searchMore ? 'search-more is-opened' : 'search-more'
                        "
                        @click="showMore"
                      >
                        <span>{{ searchMore ? '收起' : '展开' }}</span>
                        <i class="el-icon-arrow-down"></i>
                      </span>
                    </el-form-item>
                  </el-form>
                </vab-query-form-left-panel>
              </el-card>
            </vab-query-form>

            <el-card shadow="never" class="secondCard">
              <vab-query-form-right-panel style="width: 100%">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="表格筛选"
                  placement="top"
                >
                  <el-popover placement="right" trigger="click">
                    <filter-table
                      :list="filedAll"
                      :name="tableKey"
                      @updateTableShow="initTable"
                    />
                    <!-- <i class="el-icon-delete" slot="reference"></i> -->
                    <el-button
                      slot="reference"
                      icon="el-icon-s-grid"
                      class="biaoge"
                      style="margin-bottom: 10px; margin-right: 10px"
                    ></el-button>
                  </el-popover>
                </el-tooltip>
                <!-- <el-button type="success" @click="handleEdit(false, false)">
                  新建
                </el-button> -->
                <el-button type="primary">去报销</el-button>
              </vab-query-form-right-panel>

              <el-table v-loading="listLoading" :data="list">
                <el-table-column
                  align="center"
                  label="序号"
                  prop="qdcode"
                  width="100"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="订单号"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="关联申请单单号"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="服务商"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="业务场景"
                  prop="projectOrderName"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="订单类型"
                  prop="sjlxName"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="实际出行人"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="预定人"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="订单总金额"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="报销状态"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="出发城市"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="到达城市"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="出发时间"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="到达时间"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="到店日期"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="城市"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="酒店名称"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="创建时间"
                  prop="planYear"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      @click="handleEdit(scope.row, false)"
                      :disabled="!!scope.row.spzt"
                    >
                      修改
                    </el-button>
                    <el-dropdown style="margin-left: 10px">
                      <el-button type="text">更多</el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>
                          <el-button
                            @click="handleDelete(scope.row)"
                            type="text"
                            :disabled="!!scope.row.spzt"
                          >
                            删除
                          </el-button>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>

            <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </div>
        </template>
      </div>
    </template>
    <template v-if="currentIndex === 1">
      <div
        :class="[currentIndex === 1 ? 'container_right_2' : 'container_right']"
      >
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="关联申请单单号"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="订单号"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-select
                      v-model="queryForm.jsFinance"
                      placeholder="出行方式"
                    >
                      <el-option
                        v-for="item in options_1"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="出发地"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="目的地"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="实际出行人"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-date-picker
                      v-model="queryForm.day"
                      type="daterange"
                      align="right"
                      unlink-panels
                      range-separator="至"
                      start-placeholder="创建时间开始日期"
                      end-placeholder="创建时间结束日期"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-form-item>
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
                  <el-button
                    native-type="submit"
                    type="primary"
                    @click="resetSearch"
                  >
                    重置
                  </el-button>
                </el-form-item>
                <!-- <el-form-item>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      content="搜索筛选"
                      placement="top"
                    >
                      <el-popover placement="left" trigger="click">
                        <filter-search
                          v-if="true"
                          :list="searchAll"
                          :name="localKey"
                          @updateSearchShow="initSearch"
                        />
                        <el-button slot="reference" style="height: 32px">
                          <vab-icon icon="filter" :is-custom-svg="true" />
                        </el-button>
                      </el-popover>
                    </el-tooltip>
                  </el-form-item> -->
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>{{ searchMore ? '收起' : '展开' }}</span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-left-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel style="width: 100%">
            <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            >
              <el-popover placement="right" trigger="click">
                <filter-table
                  :list="filedAll"
                  :name="tableKey"
                  @updateTableShow="initTable"
                />
                <!-- <i class="el-icon-delete" slot="reference"></i> -->
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <!-- <el-button type="success" @click="handleEdit(false, false)">
                  新建
                </el-button> -->
            <el-button type="primary">去报销</el-button>
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="序号"
              prop="qdcode"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="订单号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="关联申请单单号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="服务商"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="业务场景"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="订单类型"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="实际出行人"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="预定人"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="订单总金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="报销状态"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="出发城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到达城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="出发时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到达时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到店日期"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="酒店名称"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="创建时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleEdit(scope.row, false)"
                  :disabled="!!scope.row.spzt"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        @click="handleDelete(scope.row)"
                        type="text"
                        :disabled="!!scope.row.spzt"
                      >
                        删除
                      </el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </template>
    <template v-if="currentIndex === 2">
      <div
        :class="[currentIndex === 2 ? 'container_right_2' : 'container_right']"
      >
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="关联申请单单号"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="订单号"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-select
                      v-model="queryForm.jsFinance"
                      placeholder="出行方式"
                    >
                      <el-option
                        v-for="item in options_1"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="出发地"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="目的地"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-input
                      v-model="queryForm.jsFinance"
                      clearable
                      placeholder="实际出行人"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item>
                    <el-date-picker
                      v-model="queryForm.day"
                      type="daterange"
                      align="right"
                      unlink-panels
                      range-separator="至"
                      start-placeholder="创建时间开始日期"
                      end-placeholder="创建时间结束日期"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-form-item>
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
                  <el-button
                    native-type="submit"
                    type="primary"
                    @click="resetSearch"
                  >
                    重置
                  </el-button>
                </el-form-item>
                <!-- <el-form-item>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      content="搜索筛选"
                      placement="top"
                    >
                      <el-popover placement="left" trigger="click">
                        <filter-search
                          v-if="true"
                          :list="searchAll"
                          :name="localKey"
                          @updateSearchShow="initSearch"
                        />
                        <el-button slot="reference" style="height: 32px">
                          <vab-icon icon="filter" :is-custom-svg="true" />
                        </el-button>
                      </el-popover>
                    </el-tooltip>
                  </el-form-item> -->
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>{{ searchMore ? '收起' : '展开' }}</span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-left-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel style="width: 100%">
            <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            >
              <el-popover placement="right" trigger="click">
                <filter-table
                  :list="filedAll"
                  :name="tableKey"
                  @updateTableShow="initTable"
                />
                <!-- <i class="el-icon-delete" slot="reference"></i> -->
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <!-- <el-button type="success" @click="handleEdit(false, false)">
                  新建
                </el-button> -->
            <el-button type="primary">去报销</el-button>
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="序号"
              prop="qdcode"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="订单号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="关联申请单单号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="服务商"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="业务场景"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="订单类型"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="实际出行人"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="预定人"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="订单总金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="报销状态"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="出发城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到达城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="出发时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到达时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="到店日期"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="城市"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="酒店名称"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="创建时间"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleEdit(scope.row, false)"
                  :disabled="!!scope.row.spzt"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        @click="handleDelete(scope.row)"
                        type="text"
                        :disabled="!!scope.row.spzt"
                      >
                        删除
                      </el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </template>

    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import Edit from './components/addTicketBagEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'grpj',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          startStatus: undefined,
          sxName: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
          ticketValue: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '发送方财务组织' },
          { name: '接收方财务组织' },
          { name: '发送方单据类型' },
          { name: '接收方单据类型' },
          { name: '说明' },
          { name: '协同消息接收人' },
          { name: '业务名称' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgx-fygl-grpj-search',
        tableKey: 'cwgx-fygl-grpj-list',
        searchMore: false,
        select: [],
        currentIndex: 0,
        childrenIndex: 0,
        tabs: [{ title: '待报销' }, { title: '报销中' }, { title: '已报销' }],
        childrenTabs: [{ title: '发票' }, { title: '订单' }],
        options_1: [
          {
            value: '1',
            label: '增值税普通发票',
          },
          {
            value: '2',
            label: '增值税专用发票',
          },
          {
            value: '3',
            label: '航空电子行程单',
          },
          {
            value: '4',
            label: '火车票',
          },
          {
            value: '5',
            label: '机打发票',
          },
        ],
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      selectTab(index) {
        this.currentIndex = index
      },
      selectChildrenTab(index) {
        this.childrenIndex = index
      },
      getFiled() {
        return [
          { name: '所属组织', key: 'sxName' },
          { name: '时间', key: 'startStatus' },
          { name: '收款财务组织', key: 'startStatus1' },
          { name: '付款财务组织', key: 'startStatus2' },
        ]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = false
        let { ...other } = this.queryForm
        const {
          data: { tlist, totalRecord, currProjectId },
        } = await implementPlanList({
          ...other,
        })
        this.listLoading = false
        return
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },

      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await implementPlanDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
      handleAddTicketBag() {
        console.log('11')
        this.$refs['edit'].ticketEdit()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .tab-container {
    display: flex;
    background: #fff;
  }

  .tab-item {
    padding: 10px 20px;
    border-bottom: 2px solid transparent;
    cursor: pointer;
  }

  .active {
    color: #000;
    font-weight: bold;
  }
  .line {
    width: 25px;
    height: 2px;
    background: red;
    margin: 2px auto;
  }
  .container_box {
    display: flex;
  }
  .container_left {
    width: 200px;
    padding: 15px;
    background: #fff;
  }
  .container_right {
    width: calc(100% - 200px);
  }
  .left_tabs {
    display: flex;
  }
  .left_tabs_add {
    cursor: pointer;
  }
  .piaodai {
    margin-left: 5px;
  }
  .search_input {
    margin: 10px 0;
  }
</style>
