/**
 * Created by 莫忘初衷 on 2017/1/1.
 * description: 在该js文件中定义着批量删除记录的方法.
 */

function deleteBatch(basePath) {
    // 当点击按钮触发deleteBatch的时候,通过jQuery改变#mainForm的action内容.
    $("#mainForm").attr("action", basePath + "/DeleteBatch.action");
    // 使用javascript以post的方式提交请求.
    $("#mainForm").submit();
}

/**
 * 修改当前页码，调用后台重新查询
 */
function changeCurrentPage(currentPage) {
    $("#currentPage").val(currentPage);
    $("#mainForm").submit();
}