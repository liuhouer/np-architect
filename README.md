## Java架构师直通车【慕课网 | Java架构师-技术专家 | 代码】


>项目文章https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI3MDc5Mjk1MA==&action=getalbum&album_id=1436607562610524160#wechat_redirect

关于git tag 老是忘记，tag就是一个标签，帮助我们回退到某个版本的代码，我们通过tag的名称即可回退，而不需要根据某个提冗长的commit ID来回退，算是版本记录的补充吧。这里记录下：
	- 查看本地tag：git tag 
	- 新建tag：git tag -a v2.0 -m '完成首页展示和商品详情展示'
	- 推送指定tag至远程：git push origin v2.0
	- 推送本地所有tag至远程：git push origin --tags
	- 删除本地tag：git tag -d v2.0 
	- 删除远程tag：git push origin --delete tag 2.0
	- 本地查看不同tag的代码：get checkout v1.0
	- git reset --hard  版本号  来回到最初的小程序初始化代码  （提交的版本号可以通过 git log查到）
	- 获取远程分支：git fetch origin tag V2.0

