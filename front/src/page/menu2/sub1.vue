<template lang="html">
  <div class="table-content">
    <el-table :data="tableData" size="mini" border style="width: 100%">
      <el-table-column prop="date" label="日期" width="180"></el-table-column>
      <el-table-column prop="name" label="姓名" width="180"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
    </el-table>
    <el-button size="small" type="primary" @click="dialogFormVisible = true; clearAll()" >上传个人数据</el-button>
    <el-dialog title="数据信息" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="数据名称" prop="filename">
          <el-input v-model="form.filename" placeholder="请输入数据名"></el-input>
        </el-form-item>
        <el-form-item label="数据描述" prop="description">
          <el-input type="textarea" :rows="5" placeholder="请输入数据描述" v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item label="数据价值" prop="value">
          <el-input-number v-model="form.value" oninput="value=value.replace(/[^\d.]/g,'')"></el-input-number>
        </el-form-item>
      </el-form>
      <el-upload
        class="upload-demo"
        :action=posturl
        :data="{dataname:this.form.filename,size:this.form.size,value:this.form.value,description:this.form.description}"
        ref="upload"
        multiple
        :on-preview="handlePreview"
        :limit="1"
        :auto-upload="false"
        :file-list="filelist"
        :multiple="false"
        :on-exceed="removeFile"
        :on-remove="handleRemove"
        :on-success="handleRes"
        :with-credentials="true"
      >
        <el-button slot="trigger" size="small" type="primary">选择文件上传</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      </el-upload>
    </el-dialog>
    <!--el-button size="small" type="primary" @click="handleDownload">
      点击下载
    </el-button-->
    <el-button size="small" type="primary" @click="handleDownload">
      点击下载
    </el-button>
    <pagination
      @sendPageSize="receivePageSize"
      @sendCurrentPage="receiveCurrentPage"
      :totalPage="totalPage"
    ></pagination>
  </div>

</template>

<script>
import pagination from "common/pagination";
import axios from "axios";
export default {
  data() {
    return {
      form:{
        filename:'',
        description:'',
        size:0.0,
        value:0.0
      },
      posturl:this.$global.baseUrl+"/admin/upload",
      totalPage: 300,
      filename:"面试.pdf",
      filelist:[],
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄"
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1517 弄"
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1519 弄"
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1516 弄"
        }
      ],

      dialogFormVisible:false,
      rules:{
        filename: [
          {required: true, message: '文件名不为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '文件描述不为空', trigger: 'blur'}
        ]
      },

    };
  },
  methods: {
    clearAll(){
      this.form.filelist = [];
      this.form.filename = '';
      this.form.description = '';
      this.form.value = 0;
      },
    submitUpload(){
      this.$refs.form.validate(
        valid=>{
          if(valid){
            this.$refs.upload.submit();
            //console.log(this.filelist)
            this.filelist.forEach( file=>{
              console.log(file)
            })
            this.$notify({
              title:'notification',
              message:'检查完毕，准备上传',
              type:'success'
            })
            this.timer = setTimeout(()=>{   //设置延迟执行
              console.log('ok');
            },1000);
            this.clearAll();
            this.dialogFormVisible = false;
          }else{
            this.$notify({
              title:'error',
              message:'上传失败,请检查输入是否完整',
              type:'error'
            })
          }
        }
      )

    },
    handlePreview(file){
      console.log(file)
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handleRes(res){
      console.log(res)
      if(res==='上传成功'){
        this.$notify({
          title:'notification',
          message:'上传成功',
          type:'success'
        })
      }else{
        this.$notify({
          title:'error',
          message:res,
          type:'error'
        })
      }
    },
    removeFile(){
      this.$notify({
        title:'warning',
        message:'只能上传一个文件（已清除旧文件）',
        type:'warning'
      })
      this.filelist = [];
    },
    receivePageSize(val) {
      console.log(val);
    },
    receiveCurrentPage(val) {
      console.log(val);
    },
    handleDownload(){
      /*this.$axios.get(this.$global.baseUrl + '/admin/testDownload?fileName=STAR.txt', {responseType: 'arraybuffer'})
      .then((res)=>{
        let url = window.URL.createObjectURL(new Blob([res.data]))
        let a = document.createElement('a')
        a.setAttribute("download","STAR.txt")
        a.href = url
        a.click()
      })*/
      this.$axios({
        url:this.$global.baseUrl + '/admin/testDownload',
        method:'get',
        params:{
          fileName:this.filename
        },
        responseType:'blob'
      }).then((res)=>{
        const blob = res.data
        const reader = new FileReader()
        reader.readAsDataURL(blob)
        reader.onload = (e) => {
          const a = document.createElement('a')
          a.download = this.filename
          a.href = e.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
        }
      }).catch((error)=>console.log(error))
    },
  },
  components: {
    pagination
  }
};
</script>

<style scoped lang="scss">
.table-content{
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
