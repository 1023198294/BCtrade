<template>
  <!--p class="page-container">404 page not found</p-->
  <div class="register-box">
    <div class="register-page-container">
      <el-form
        ref="ruleForm3"
        :model="ruleForm3"
        :rules="rules3"
        label-position="left"
        label-width="0px"
        class="register-container"
      >
        <h3 class="title">注册用户</h3>
        <el-form-item prop="inusername">
          <el-input v-model="ruleForm3.inusername" type="text" placeholder="请输入用户名"/>
        </el-form-item>

        <el-form-item prop="inpassword">
          <el-input v-model="ruleForm3.inpassword" type="text" placeholder="请输入密码"/>
        </el-form-item>

        <el-form-item prop="repassword">
          <el-input v-model="ruleForm3.repassword" type="text" placeholder="请确认密码"/>
        </el-form-item>
        <el-form-item prop="isadmin">
          <el-checkbox v-model="ruleForm3.isadmin">是否管理</el-checkbox>
        </el-form-item>
        <el-form-item style="width: 100%">
          <el-button :loading="registering" type="primary" style="width: 100%" @click="checkAndSubmit">
            提交
          </el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>
<script>
  import {mapMutations} from 'vuex'
  import * as types from '../store/mutation-types'

  export default {
    props:{},
    data(){
      var validatePass =(rule,value,callback)=>{
        if(value===''){
          callback(new Error("请再次输入密码"));
        }else if(this.ruleForm3.inpassword!==value){
          callback(new Error("两次输入密码不一致"));
        } else{
          callback();
        }
      };
      return{
        registering : false,
        ruleForm3: {
          inusername:'',
          inpassword:'',
          repassword:'',
          isadmin:false,
        },
        rules3:{
          inusername:[
            {
              required:true,
              message:"请输入账号",
              trigger:"blur"
            }
          ],
          inpassword: [
            {
              required: true,
              message:"请输入密码",
              trigger:"blur"
            }
          ],
          repassword: [
            {
            validator:validatePass,
            trigger:"blur"
          }]
        },

      }
    },
    methods:{
      checkAndSubmit(){
        this.$refs.ruleForm3.validate(valid=>{
          if(valid){
            this.registering = true
            //console.log('valid submit')
            const params = {
              id:0,
              username:this.ruleForm3.inusername,
              password:this.ruleForm3.inpassword,
              available:true,
              role:this.ruleForm3.isadmin?"admin":"user",
              regtime:"0"
            }
            setTimeout(()=>{
              this.$axios(
                {
                  method:'post',
                  url:this.$global.baseUrl+'/admin/register',
                  data:params
                }
              ).then((response)=>{
                console.log(response);
                if(response.data.toString()[0]==="用"){
                  this.$message("用户名已存在，请重新输入")
                  this.registering = false
                  //this.sucess = false;
                }else if(response.data.toString()[0]==="注"){
                  this.registering = false
                  this.$message("成功注册!")
                  //this.sucess = true;
                  this.$router.push({path:'/login'})
                }
              }).catch((error)=>console.log(error))
            },20000)
          }else{
            console.log('error submit')
            return false
          }
        })
      }
    }
  }
</script>
<style scoped lang="scss">
  @import "../assets/css/them.scss";
  .register-box{
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0;
    top: 0;
    background-color: $globalBgColor;
  }
  .register-container{
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
</style>
