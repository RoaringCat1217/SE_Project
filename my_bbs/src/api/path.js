//这里可以存储网络请求涉及到的的各个路径
const base = {
    baseUrl: '/api',    //端口号
    login:'/login',     //登录
    register:'/register',   //注册
    hotlist:'/hot',     //热榜
    like:'/like',       //点赞
    star:'/star',       //收藏
    getStars:'/getstars',   //获取收藏列表
    groups:'/catalogue',    //获取版面目录
    groupInfo:'/group',     //获取版面内容
    getReply:'/getreply',   //获取帖子回复
    getPost:'/getpost',     //获取帖子内容
    getAvatar:'/getavatar', //获取头像
    updateUserInfo:'/updateuserinfo',   //更新用户信息
    getUserInfo:'/getuserinfo',     //获取用户信息
    newPost:'/newpost',     //发帖
    newComment:'/newcomment',    //发表评论(回复)
    search:'/search'

}

export default base;