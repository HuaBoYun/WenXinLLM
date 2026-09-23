<template>
  <div class="course-con">
    <div class="finance">
      <h2>财经学堂</h2>
      <p class="desc">线上线下一体化云学习平台</p>
      <div class="maintitle-con">
        <el-row class="maintitle" :gutter="5">
          <el-col v-for="(item, index) in maintitlelist" :key="index" :span="4">
            <div
              class="grid-content bg-purple"
              @mouseout="removeActive($event)"
              @mouseover="changeActive($event)"
            >
              <vab-icon :icon="item.iconname" />
              <h3 class="font-weight-300">{{ item.titlename }}</h3>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="mainimglist">
        <ul v-for="(item, index) in oneimg" :key="index">
          <li>
            <img src="../../../assets/index_img/one-1.jpg" />
            <img src="../../../assets/index_img/one-2.jpg" />
          </li>
        </ul>
      </div>
    </div>
    <div class="scene">
      <h2>场景化分析</h2>
      <div class="maintitle-con">
        <ul class="maintitle" :gutter="5">
          <li v-for="(item, index) in scene" :key="index">
            <div
              class="grid-content bg-purple"
              @mouseout="removeActive($event)"
              @mouseover="changeActive($event)"
            >
              <h3 class="font-weight-300">{{ item.titlename }}</h3>
            </div>
          </li>
        </ul>
      </div>
      <div class="mainimglist">
        <ul v-for="(item, index) in oneimg" :key="index">
          <li>
            <img src="../../../assets/index_img/one-1.jpg" />
            <div class="project-intro">
              <h4 class="project-title"><span style="">风险评估报告</span></h4>
              <!-- <div class="project-descr">大型百货管理系统实现集团总部管理控制、物流配送、智能决策分析...</div> -->
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="container">
      <img class="bottombg" src="../../../assets/index_img/bottom-bg.jpg" />
      <div class="title-box">
        <div class="head">
          <h2>微咨询</h2>
          <p class="desc">权威财经行业专家联合打造微咨询平台</p>
        </div>
        <ul>
          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="user-3-fill">
              <span class="num">01</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">人力资源</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>

          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="trophy-fill">
              <span class="num">02</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">风险管控</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>
          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="newspaper-fill">
              <span class="num">03</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">财务管理</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>
          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="money-cny-circle-fill">
              <span class="num">04</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">税务管理</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>
          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="surgical-mask-fill">
              <span class="num">05</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">法律服务</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>

          <li class="item" style="visibility: inherit; opacity: 1">
            <vab-icon icon="survey-fill">
              <span class="num">06</span>
            </vab-icon>
            <h3 class="m-t-20 m-b-5 font-weight-300">ERP规划</h3>
            <p class="m-b-0 font-weight-300"></p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Course',
    data() {
      return {
        maintitlelist: [
          { titlename: '财经论坛', iconname: 'currency-fill' },
          { titlename: '管理会计', iconname: 'folder-shield-2-fill' },
          { titlename: '企业风控', iconname: 'codepen-fill' },
          { titlename: '政府内控', iconname: 'database-fill' },
          { titlename: '内部审计', iconname: 'coreos-fill' },
          { titlename: '法律合规', iconname: 'scales-3-fill' },
        ],
        scene: [
          { titlename: '全部' },
          { titlename: '供应商监控' },
          { titlename: '业财风险监控' },
          { titlename: '合同风险监控' },
          { titlename: 'ERP表单监控' },
        ],
        oneimg: [
          { imgsrc: 'one-1' },
          { imgsrc: 'one-2' },
          { imgsrc: 'one-3' },
          { imgsrc: 'one-4' },
          { imgsrc: 'one-5' },
        ],
        tabNum: 1,
      }
    },
    methods: {
      changeTab(num) {
        this.tabNum = num
      },
      changeActive($event) {
        $event.currentTarget.className = 'grid-content bg-purple cur'
      },
      removeActive($event) {
        $event.currentTarget.className = 'grid-content bg-purple'
      },
    },
  }
</script>

<style lang="scss" scoped>
  .course-con {
    h2 {
      padding: 30px 0 0 0;
      margin-bottom: 2px;
      font-size: 32px;
      color: #000;
      text-align: center;
      text-transform: uppercase;
    }
    .container {
      position: relative;
      width: 100%;
      margin-top: 20px;
      .bottombg {
        position: absolute;
        width: 100%;
      }
      /*background: url('~@/asset/index_img/bottom-bg.jpg') no-repeat center;*/
      /*background-size: cover;*/
      .title-box {
        position: absolute;
        width: 80%;
        margin-left: 10%;
        h2 {
          padding: 0;
          font-size: 30px;
          font-weight: 400;
          color: #ffffff;
          text-align: center;
        }
        .desc {
          width: 80%;
          margin: 0 auto;
          font-size: 18px;
          color: #ffffff;
          text-align: center;
        }
      }
      ul {
        position: absolute;
        width: 100%;
        list-style: none;
        li {
          float: left;
          width: 16.66%;
          margin: 10px 0 0;
          text-align: center;
          i {
            position: relative;
            width: 120px;
            height: 120px;
            font-size: 50px;
            line-height: 120px;
            color: #ffffff;
            text-align: center;
            border: 1px solid;
            border-color: #ffffff;
            border-radius: 100%;
            -webkit-transition: all 0.25s ease-out;
            transition: all 0.25s ease-out;
            &:hover {
              color: #f9373f;
              background: #fff;
            }
          }
          h3 {
            color: #fff;
            text-align: center;
          }
        }
      }
    }
    .scene {
      height: 300px;
      margin-top: 20px;
      .maintitle-con {
        width: 70%;
        margin: 0 auto;
        .maintitle {
          overflow: hidden;
          list-style: none;
          .grid-content {
            float: left;
            width: 20%;
            color: #3ba4c1;
            text-align: center;
            h3 {
              display: block;
              height: 50px;
              margin: 0;
              font-size: 14px;
              line-height: 50px;
            }
          }
          .cur {
            color: #fff;
            background: #f9373f;
          }
        }
      }
      .mainimglist {
        width: 100%;
        ul {
          padding: 0;
          margin: 0;
          list-style: none;
          li {
            position: relative;
            float: left;
            width: 20%;
            img {
              width: 100%;
            }
            .project-intro {
              position: absolute;
              top: 0;
              left: 0;
              box-sizing: border-box;
              width: 80%;
              height: 80%;
              margin: 10%;
              text-align: center;
              background: #000;
              opacity: 0;
              .project-title {
                padding: 10px;
                margin: 0 0 20px 0;
                font-size: 14px;
                font-weight: 600;
                line-height: 1.3;
                color: #fff;
                text-align: center;
              }
              &:hover {
                opacity: 1;
                transition: all 0.5s;
              }
            }
          }
        }
      }
    }
    .finance {
      height: 574px;
      .desc {
        padding: 0 0 30px 0;
        font-size: 24px;
        text-align: center;
      }
      .maintitle-con {
        width: 70%;
        margin: 0 auto;
        .maintitle {
          overflow: hidden;
          list-style: none;
          .grid-content {
            position: relative;
            color: #3ba4c1;
            text-align: center;
            i {
              font-size: 50px;
            }
            h3 {
              display: block;
              height: 50px;
              margin: 0;
              font-size: 14px;
              line-height: 50px;
            }
            &:hover {
              color: #f9373f;
            }
            &:after {
              position: absolute;
              bottom: -20px;
              left: 45%;
              display: block;
              width: 0;
              height: 0;
              margin-left: -10 px;
              content: '';
              border-right: 10px solid transparent;
              border-bottom: 10px solid #d1d1d1;
              border-left: 10px solid transparent;
              transition: all 0.5s;
            }
          }
          .cur {
            &:after {
              bottom: 0;
              transition: all 0.5s;
            }
          }
        }
      }
      .mainimglist {
        width: 100%;
        ul {
          padding: 0;
          margin: 0;
          list-style: none;
          li {
            float: left;
            width: 20%;
            img {
              width: 100%;
            }
          }
        }
      }
    }
  }
</style>
