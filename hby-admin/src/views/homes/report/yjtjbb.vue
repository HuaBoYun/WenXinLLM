<template>
  <div class="system-log-container">
    <div class="content">
      <div class="banner">
        <img :src="bannerImg" alt="" />
      </div>
      <el-row :gutter="10">
        <el-col :span="8" v-for="item in allDatas" :key="item.title">
          <el-card>
            <h3 class="title">{{ item.title }}</h3>
            <el-table :data="item.tableData">
              <el-table-column prop="avator" label="排名" #default="{ $index }">
                <div class="name-content">
                  <div class="rank-img">
                    <img v-if="rankImg[$index]" :src="rankImg[$index]" alt="" />
                    <div v-else class="rank-num">{{ $index + 1 }}</div>
                  </div>
                  <div class="avator-img">
                    <img :src="avatorImgs[$index]" alt="" />
                  </div>
                </div>
              </el-table-column>
              <el-table-column prop="name" label="人员">
                <template #default="{ row }">
                  <div class="name-content">
                    <a href="#">{{ row.name }}</a>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="money" label="金额">
                <template #default="{ row, $index }">
                  <div class="name-content" :class="'money' + ($index + 1)">
                    {{ row.money }}万元
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  const img1 = require('@/assets/indexPage/report/yjtjbb/01.png')
  const img2 = require('@/assets/indexPage/report/yjtjbb/02.png')
  const img3 = require('@/assets/indexPage/report/yjtjbb/03.png')
  const bannerImg = require('@/assets/indexPage/report/yjtjbb/banner@2x-2.png')
  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        rankImg: [img1, img2, img3],
        bannerImg: bannerImg,
        avatorImgs: [
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd23b7386a63a8a2f4373b9985&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd065c51d7dbc939645baae43b&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f3773f85ae7040f5364822aebc3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f1e3143101485ff190409f8bd3d&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f0fe13f6294d9af17847068783f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18edc348aff78adb879a423594b1f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f61d8789d0bc80913d44a5b44c3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd303ca9ac47a70c444eab571b&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f580a3ed10e12c2c774b358b78f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd2162c02fbb241ba404393aa9&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd1f033c961e9f1234c628ecf2&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0d86abb1179f91333214487094821&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f3773f85ae7040f5364822aebc3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f1e3143101485ff190409f8bd3d&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd0c86519b572d47245c195510&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd09110a1c9f2dd8148a59930f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f61d8789d0bc80913d44a5b44c3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd303ca9ac47a70c444eab571b&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f580a3ed10e12c2c774b358b78f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd2162c02fbb241ba404393aa9&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd303ca9ac47a70c444eab571b&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd23b7386a63a8a2f4373b9985&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f1e3143101485ff190409f8bd3d&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f3773f85ae7040f5364822aebc3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd09110a1c9f2dd8148a59930f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f580a3ed10e12c2c774b358b78f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f0fe13f6294d9af17847068783f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18edc348aff78adb879a423594b1f&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a18f61d8789d0bc80913d44a5b44c3&size=b',
          'https://ekpdemo.landray.com.cn/sys/person/image.jsp?personId=17a0dbfd303ca9ac47a70c444eab571b&size=b',
        ],
        allDatas: [
          {
            title: '本季销售人员有效金额龙虎榜',
            tableData: [
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
            ],
          },
          {
            title: '本季项目经理有效验收金额龙虎榜',
            tableData: [
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
            ],
          },
          {
            title: '本季客服专员常规有效回款龙虎榜',
            tableData: [
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
              { name: '张孝昆', bel: '华南大区_销售部', money: 1391 },
            ],
          },
        ],
      }
    },
    created() {},
    methods: {},
  }
</script>
<style scoped>
  .system-log-container {
    padding: 0 !important;
  }

  .banner {
    height: 145px;
    margin-bottom: 20px;
  }

  img {
    width: 100%;
    height: 100%;
  }

  .name-content {
    display: flex;
    align-items: center;
  }

  .rank-img {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 20px;
  }

  .rank-num {
    color: #737c8b;
    background-color: #f6f6f6;
    line-height: 22px;
    text-align: center;
  }

  .avator-img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
  }

  .money1 {
    color: #f15e47;
  }
  .money2 {
    color: #ffc200;
  }
  .money3 {
    color: #f5a623;
  }
</style>
