<!--
 * @Description:
 * @Autor: GaoSong
 * @Date: 2021-01-02 12:29:38
 * @LastEditors: GaoSong👉😜👈
 * @LastEditTime: 2021-01-02 14:49:48
-->

<template>
  <div class="container-box">

    <!--el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="用户管理" name="first">用户管理</el-tab-pane>
      <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>
      <el-tab-pane label="角色管理" name="third">角色管理</el-tab-pane>
      <el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>
      <el-button v-throttle-click="[nodeClick, '666']">这是一个按钮</el-button>
    </el-tabs-->
    <el-container>
      <el-aside width="200px">
        Description of BCToken
      </el-aside>
      <el-main  >
        <div style="margin-top: 150px;margin-bottom: 20px">请输入充值金额</div>
        <el-form>
          <el-form-item
            label="充值金额"
            rules="[{ required: true, message: '请填写Token额，仅限正数浮点数'},{pattern:/^\\d+(\\.\\d+)?$/, message: '金额正数浮点数'}]"
            prop="money">
            <el-input
                      v-model="input"
                      placeholder="金额大小"
                      style="width: 50%"
                      @change="check"
                    auto-complete="false">
            </el-input>
          </el-form-item>

        </el-form>

        <el-button :loading="submitting" type="primary" style="width: 20%" @click="checkAndSubmit" >
          提交
        </el-button>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import { _typeof } from '@/utils/utils.js'
export default {
  data() {

    return {
      submitting:false,
      input:'',
      activeName: 'second',
      rules:{
        money:[
          {required:true,message:'请输入金额大小'},

        ]
      }
    }
  },
  mounted() {
    const num = 666
    console.log(_typeof.isNumeric(num))
    // window.addEventListener('mousemove', throttle(this.nodeClick, 1000, 666, 888))
  },

  methods: {

    handleClick(tab, event) {
      console.log(tab, event)
    },
    nodeClick(...val) {
      console.log(222, val)
    }
  }
}
</script>

<style lang="css">
</style>
