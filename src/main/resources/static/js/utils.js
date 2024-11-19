+function () {
	/// 常用工具
	window.utils = {
		// 判断是否为空
		isEmpty: function (obj) {
			return obj === undefined || obj === null || obj.length === 0;
		},
		// 判断是否是函数
		isFunction: function (func) {
			return !utils.isEmpty(func) && func.constructor === Function;
		},
		// 自定义事件列表
		customEvent: {},
		// 添加自定义时间
		addEvent: function (eventName, func) {
			if (utils.isEmpty(eventName) || !utils.isFunction(func)) {
				return;
			}
			let events = this.customEvent[eventName];
			if (utils.isEmpty(events)) {
				this.customEvent[eventName] = [func];
			} else {
				this.customEvent[eventName].push(func);
			}
		},
		// 触发自定义事件
		fireEvent: function (eventName, param1, param2, param3) {
			if (utils.isEmpty(eventName)) {
				return;
			}
			let eventFuncs = this.customEvent[eventName];
			if (!utils.isEmpty(eventFuncs)) {
				for (let idx in eventFuncs) {
					let func = eventFuncs[idx];
					if (utils.isFunction(func)) {
						func(param1, param2, param3);
					}
				}
			}
			this.removeEvent(eventName);
		},
		// 删除自定义事件
		removeEvent: function (eventName) {
			if (utils.isEmpty(eventName)) {
				return;
			}
			this.customEvent[eventName] = undefined;
		}
	}

	/// 加解密
	window.encp = {
		key: CryptoJS.enc.Utf8.parse("zhgerXHBVaaKm8xy"),
		// 加密
		encrypt: function (plaintText) {
			let encryptObj = CryptoJS.AES.encrypt(plaintText, this.key, {
				mode: CryptoJS.mode.ECB,
				padding: CryptoJS.pad.Pkcs7
			});
			return encryptObj.toString();
		},
		// 解密
		decrypt: function (cipherText) {
			let decryptedData = CryptoJS.AES.decrypt(cipherText, this.key, {
				mode: CryptoJS.mode.ECB,
				padding: CryptoJS.pad.Pkcs7
			});
			return decryptedData.toString(CryptoJS.enc.Utf8);
		}
	}

	/// 常用数据结构
	// 不重复数据Set
	window.TreeSet = function () {
		this.data = [];
	}

	window.TreeSet.prototype = {
		add: function (element) {
			if (this.contains(element)) {
				return false;
			}
			this.data.push(element);
			return true;
		},
		remove: function (element) {
			let index = this.indexOf(element);
			if (index > -1) {
				this.data.splice(index, 1);
				return true;
			}
			return false;
		},
		each: function (callback) {
			if (utils.isEmpty(callback) || callback.constructor !== Function) {
				return;
			}
			for (let i = 0; i < this.size(); i++) {
				callback(i, this.data[i]);
			}
		},
		size: function () {
			return this.data.length;
		},
		get: function (index) {
			if (index > this.size() - 1) {
				throw new Error('Out of index from TreeSet.');
			}
			return this.data[index];
		},
		contains: function (element) {
			return this.indexOf(element) !== -1;
		},
		indexOf: function (element) {
			for (let i = 0; i < this.data.length; i++) {
				if (this.data[i] === element) {
					return i;
				}
			}
			return -1;
		},
		isEmpty: function () {
			return this.size() === 0;
		},
		/**
		 * 判断TreeSet中每个元素是否都在传入数组中
		 * @param elements
		 */
		obtainAll: function (elements) {
			for (let i = 0; i < this.size(); i++) {
				if (!elements.contains(this.get(i))) {
					return false;
				}
			}
			return true;
		}
	}

	/// TreeMap
	window.TreeMap = function (...entry) {
		this.key = new TreeSet();
		this.data = {};
		if (entry.constructor === Array && !utils.isEmpty(entry)) {
			for (let i = 0; i <= entry.length / 2; i += 2) {
				this.put(entry[i], entry[i + 1]);
			}
		}
	}

	window.TreeMap.prototype = {
		put: function (key, value) {
			this.key.add(key);
			this.data[key] = value;
		},
		get: function (key) {
			return this.data[key];
		},
		remove: function (key) {
			this.key.remove(key);
			delete this.data[key];
		},
		size: function () {
			return this.key.size();
		},
		each: function (callback) {
			if (utils.isEmpty(callback) || callback.constructor !== Function) {
				return;
			}
			for (const dataKey in this.data) {
				callback(dataKey, this.data[dataKey]);
			}
		},
		containsKey: function (key) {
			return !utils.isEmpty(this.get(key));
		},
		containsValue: function (value) {
			for (const dataKey in this.data) {
				if (this.data[dataKey] === value) {
					return true;
				}
			}
			return false;
		}
	}

	/// DMap
	window.DMap = function () {
		this.data = [];
	}

	DMap.fn = DMap.prototype;
	DMap.fn.add = function (x, y) {
		let xData = this.data[x];
		if (xData === undefined) {
			this.data[x] = {};
		}
		this.data[x][y] = 1;
	}
	DMap.fn.get = function (x, y) {
		let xData = this.data[x];
		if (xData === undefined) {
			return undefined;
		}
		return this.data[x][y];
	}
	DMap.fn.exists = function (x, y) {
		let xData = this.data[x];
		if (xData === undefined) {
			return false;
		}
		return this.data[x][y] !== undefined;
	}

	/// jQuery扩展

	/// 原生js扩展
	/**
	 * 判断数组是否存在指定元素
	 * @param element
	 * @returns {boolean}
	 */
	Array.prototype.contains = function (element) {
		for (let i = 0; i < this.length; i++) {
			if (this[i] === element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取数组中最小的元素
	 */
	Array.prototype.min = function (comparator) {
		if (this.length === 0) {
			return null;
		}
		if (comparator && utils.isFunction(comparator)) {
			let result = this[0];
			for (let i = 1; i < this.length; i++) {
				if (comparator(result, this[i]) > 0) {
					result = this[i];
				}
			}
			return result;
		}

		return Math.min.apply(null, this);
	}

	/**
	 * 获取数组中最大的元素
	 */
	Array.prototype.max = function (comparator) {
		if (this.length === 0) {
			return null;
		}
		if (comparator && utils.isFunction(comparator)) {
			let result = this[0];
			for (let i = 1; i < this.length; i++) {
				if (comparator(result, this[i]) < 0) {
					result = this[i];
				}
			}
			return result;
		}
		return Math.max.apply(null, this);
	}

	/**
	 * 数组排序
	 * @param comparator
	 */
	Array.prototype.sorts = function (comparator) {
		if (this.length === 0) {
			return;
		}
		if (comparator && utils.isFunction(comparator)) {
			for (let i = 0; i < this.length; i++) {
				for (let j = 1; j < this.length; j++) {
					if (i === j) {
						continue;
					}
					if (comparator(this[i], this[j]) < 0) {
						let temp = this[i];
						this[i] = this[j];
						this[j] = temp;
					}
				}
			}
		} else {
			this.sort();
		}
	}
}();