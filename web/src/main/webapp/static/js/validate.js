Array.prototype.in_array = function(e) {
	for (i = 0; i < this.length && this[i] != e; i++)
		;
	return !(i == this.length);
}

$.validator.setDefaults({
	highlight : function(element) {
		$(element).closest('.form-group').addClass('has-error');
	},
	unhighlight : function(element) {
		$(element).closest('.form-group').removeClass('has-error');
	},
	errorElement : 'span',
	errorClass : 'help-block',
	errorPlacement : function(error, element) {
		if (element.parent('.input-group').length) {
			error.insertAfter(element.parent());
		} else {
			error.insertAfter(element);
		}
	}
});

$.validator.addMethod("taskNameTest", function(value, element, params) {
	var p = /^(?!_)[0-9a-zA-Z|_|\.|\-|(|)]*$/;
	if (p.test(value))
		return true;
	return false;
}, "名称中可使用的字符包括：数字、字母、下划线、横线、点和括号,但不能以下划线开头");

$.validator.addMethod("validName", function(value, element, params) {
	var result = false;
	$.ajax({
		url : '/task/create/name_exist?name=' + $("#name").val(),
		type : 'get',
		success : function(data) {
			if (data.exist == true) {
				result = false;
			} else {
				result = true;
			}
		},
		error : function(data) {
			result = false;
		},
		dataType : "json",
		async : false
	});

	return result;
}, "该名字已经存在！");

$.validator.addMethod("isNormalInteger", function(value, element, params) {
	return /^\+?(0|[1-9]\d*)$/.test(value);
}, "必须为非负整数");

$.validator.addMethod("isPositiveInteger", function(value, element, params) {
	return /^\+?([1-9]\d*)$/.test(value);
}, "必须为正整数");

$.validator.addMethod("isValidateFile", function(value, element, params) {
	if(value!=""){
		var p = /\.zip|\.sh|\.ZIP|\.SH$/;
		if (p.test(element.files[0].name))
			return true;
		return false;
	}
	return true;
}, "您选择的文件类型不合法，请上传zip或sh文件！");

$.validator.addMethod("isNullFile", function(value, element, params) {
	if(value!=""){
		//先判断文件大小是否为0，为0则说明文件内容为空或文件不存在
		if(element.files[0].size == 0){
			return false;
		}
		return true;
	}
	return true;
}, "文件不存在或内容为空，请重新选择！");

$.validator.addMethod("maxUploadFile", function(value, element, params) {
	if(value!=""){
		//先判断文件大小是否为0，为0则说明文件内容为空或文件不存在
		if(element.files[0].size/(1024*1024) > 100){
			return false;
		}
		return true;
	}
	return true;
}, "文件大小不可超过100M，请重新选择！");

$('#form').validate({
	rules : {
		name : {
			minlength : 2,
			required : true,
			taskNameTest : true,
			validName : true
		},
		command : {
			required : true
		},
		calloutUrl : {
			required : true
		},
		timeout : {
			required : true,
			isPositiveInteger : true
		},
		retryTimes : {
			required : true,
			isNormalInteger : true
		},
		crontab:{
			required: true
		},
		fileUpload:{
			isValidateFile: true,
			isNullFile: true,
			maxUploadFile: true
		},
		agent:{
			required: function(e){
				return $("input[name='executeType'][value='single']").prop('checked');
			}
		}
	}
});
