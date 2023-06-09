//2011-01-16  wxh 編輯
(function($){ 
	//遮罩方法mask
	$.extend({ 
		mask: function(options){ 
			// 扩展参数 
			var op = $.extend({ 
				opacity: 0.8, 
				z: 10, 
				bgcolor: '#000' 
				}, options); 
			// 创建一个 Mask 层，追加到 document.body 
			$('<div class="jquery_addmask"> </div>').appendTo(document.body).css({ 
			position: 'absolute', 
			top: '0px', 
			left: '0px', 
			'z-index': op.z, 
			width: $(document).width(), 
			height: $(document).height(), 
			'background-color': op.bgcolor, 
			opacity: 0 
			}).fadeIn('slow', function(){ 
					// 淡入淡出效果 
					$(this).fadeTo('slow', op.opacity); 
				}); 
			return this; 
		},
		unmask: function(){//撤銷遮罩方法unmask
			$(".jquery_addmask").fadeTo('slow', 0, function(){ 
				$(this).remove(); 
			}); 
			return this;
		}
	}); 
})(jQuery); 