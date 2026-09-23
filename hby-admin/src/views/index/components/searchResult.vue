<template>
    <div class="app">
        <div class="headerBoxs">
            <div class="headers">
                <div class="contents">
                    <div class="searchBoxs">
                        <el-autocomplete style="width: 100%;" class="inline-input" v-model="searchValue" placeholder="请输入内容">
                        </el-autocomplete>
                        <el-button type="primary">搜 索</el-button>
                    </div>
                </div>
                <div class="rights">
                    <div class="listBoxBtns">
                        <div class="listBoxBtnLefts">热搜<i class="el-icon-arrow-right"></i></div>
                        <div class="listBoxBtnRights"><i class="el-icon-refresh"></i>换一换</div>
                    </div>
                    <div class="listItems" v-for="(item, index) in list" :key="index" @click="searchValue = item"><span class="nums"
                            :style="{ color: index == 0 ? '#fe2d46' : index == 1 ? '#f60' : index == 2 ? '#faa90e' : '' }">
                            {{ index + 1 }}</span><span class="titles">{{ item }}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="results">
            <div class="lefts">
                <div class="navBoxs">
                    <div :class="[navIndex == 0 ? 'navItems navItemActiveds' : 'navItems']" @click="navIndex = 0"><i class="el-icon-search"></i>网页</div>
                    <div :class="[navIndex == 1 ? 'navItems navItemActiveds' : 'navItems']" @click="navIndex = 1"><i class="el-icon-postcard"></i>资讯</div>
                    <div :class="[navIndex == 2 ? 'navItems navItemActiveds' : 'navItems']" @click="navIndex = 2"><i class="el-icon-data-line"></i>视频</div>
                    <div :class="[navIndex == 3 ? 'navItems navItemActiveds' : 'navItems']" @click="navIndex = 3"><i class="el-icon-picture-outline"></i>图片</div>
                    <div :class="[navIndex == 4 ? 'navItems navItemActiveds' : 'navItems']" @click="navIndex = 4"><i class="el-icon-collection"></i>文库</div>
                </div>
                <div class="leftItems" v-for="i in 20" :key="i">
                    <div class="leftTitles"><span>街采</span>夫妻被拒后劝其分手 <span>青岛电视台</span>一主持人被停职</div>
                    <div class="leftContents">近日,<span>青岛电视台</span>《一见钟情》栏目组因一起<span>主持人言辞不当</span>事件引发了广泛关注。据悉,一对夫妻在商场被该栏目组采访,婉拒后却遭到了主持人的语言攻击,并被劝说“分手”...</div>
                    <div class="leftAuthors">站长之家</div>
                </div>
                <div style="height: 20px;"></div>
            </div>
        </div>
    </div>
</template>

<script>
import Logo from '@/assets/logo.png'
export default {
    data() {
        return {
            searchValue: '',
            list: ['相向而行的力量', '小城市何以撬动文旅大市场', '谢娜曾反对张杰去歌手', '男生地铁上被女子偷拍追上硬刚', '西安一副镇长半夜叫别人妻子唱歌', '菏泽郭有才为什么就火了'],
            navIndex: 0
            
        };
    },
    mounted() {

    },
    methods: {

    }
}
</script>

<style scoped>
.app {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
}
.headerBox {
    width: calc(100vw - 20px);
    height: 65px;
    /* box-shadow: 0 2px 10px 0 rgba(0,0,0,.1); */
    display: flex;
    justify-content: center;
}
.header {
    width: 1200px;
    padding: 10px;
    display: flex;
    position: fixed;
    top: 0px;
    padding-top: 15px;
    background-color: #ffffff;
    z-index: 99;
    /* justify-content: space-between; */
}

.logo {
    width: 40px;
    margin-right: 10px;
}

.content {
    width: 655px;
}

.searchBox {
    display: flex;
}

.searchBox .el-input__inner {
    border-radius: 4px 0 0 4px !important;
}

.searchBox .el-button {
    border-radius: 0 4px 4px 0 !important;
}
.login {
    flex: 1;
    text-align: right;
}
.navBox {
    display: flex;
    margin-bottom: 10px;
}
.navItem {
    color: #626675;
    margin-right: 27px;
    line-height: 28px;
    text-align: left;
    margin-top: 4px;
    font-size: 14px;
    cursor: pointer;
}
.navItemActived {
    border-bottom: 2px solid #38f;
}
.navItem i {
    font-size: 13px;
    margin-right: 2px;
}
.result {
    width: 1200px;
    max-height: calc(100vh - 65px);
    /* padding-top: 10px; */
    display: flex;
    justify-content: space-between;
    position: relative;
}
.left {
    width: 600px;
}
.leftItem {
    margin-bottom: 20px;
}
.leftTitle {
    color: #2440B3;
    text-decoration: underline;
    font-size: 18px;
    cursor: pointer;
}
.leftTitle span{
    color: #F73131;
}
.leftContent {
    font-size: 13px;
    color: #333333;
    line-height: 21px;
    margin-top: 4px;
}
.leftContent span{
    color: #F73131;
}
.leftAuthor {
    font-size: 13px;
    color: #626675;
    margin-top: 2px;
}
.right {
    width: 370px;
    position: absolute;
    top: 98px;
    right: 0px;
}
.listItem {
    width: 370px;
    height: 32px;
    line-height: 32px;
    font-size: 13px;
    color: #2440b3;
    cursor: pointer;
}

.listItem .num {
    margin-right: 10px;
    font-size: 15px;
    color: #9195a3;
}

.listItem:hover>.title {
    color: #315efb;
    text-decoration: underline;
}

.listBoxBtn {
    display: flex;
    flex-wrap: wrap;
    width: 300px;
    padding-top: 30px;
    justify-content: space-between;
}

.listBoxBtnLeft {
    font-size: 15px;
    color: #222222;
    font-weight: bold;
    cursor: pointer;
}

.listBoxBtnRight {
    font-size: 14px;
    color: #626675;
    cursor: pointer;
}

.listBoxBtnLeft:hover {
    color: #315efb;
}

.listBoxBtnRight:hover {
    color: #315efb;
}
</style>