var selfServiceApp = angular.module('selfServiceApp', ['ngMessages']);

selfServiceApp.service('payrollService', [ '$http', '$q', function($http, $q) {

	this.publishPostRequest = function(fd, url) {
		var deferred = $q.defer();
		$http.post(url, fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		})
		.then(function (response) {
			deferred.resolve(response.data);
		}, function (errResponse) {
			deferred.reject(errResponse.data);
		});
		return deferred.promise;
	}
	
	this.publishGetRequest = function(url) {
		var deferred = $q.defer();
        $http.get(url)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                 deferred.reject(errResponse);
            }
        );
        return deferred.promise;
	}
} ]);

selfServiceApp.controller('fileUploadController', [ '$scope', 'payrollService', function($scope, payrollService) {
	$scope.report = {};
	var url = "http://localhost:9082/wave-payroll/payroll"
	
	$scope.category_changed = function() {
		$scope.status = "";
		$scope.reports = "";
		$scope.report = {};
	}
	
	$scope.file_changed = function(element) {
		$scope.$apply(function(scope) {
			
			if((element.files[0].size/1024)/1024 > 10) {
				$scope.status = "File size cannot be greater than 10MB.";
				return;
			}
			
			var fd = new FormData();
			fd.append('file', element.files[0]);
			payrollService.publishPostRequest(fd, url + "/upload")
			.then(function(data) {
				$scope.reports = data;
				$scope.status = "";
				element.files="";
			}, function(errorData) {
				$scope.reports = "";
				$scope.status = errorData.errorMessage;
			});
		});
	};

	$scope.submit = function() {
		payrollService.publishGetRequest(url + "/report?reportId=" + $scope.report.reportId)
		.then(function (response) {
			$scope.reports = response;
			if($scope.reports.length==0) {
					$scope.status = "The Report ID entered does not exist.";
				} else {
					$scope.status = "";
				}
			}, function (errorData) {
				$scope.reports = [];
				$scope.status = "There was an error in fetching the report";
		});
	}	
} ]);
