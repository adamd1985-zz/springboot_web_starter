angular
	.module('hello', [ 'ngRoute' ])
		.config(function($routeProvider, $httpProvider) {
			$routeProvider.when('/', {
				templateUrl 	: 'home.html',
				controller 		: 'home',
				controllerAs	: 'controller'
			}).when('/login', {
				templateUrl 	: 'login.html',
				controller 		: 'navigation',
				controllerAs	: 'controller'
			}).otherwise('/');

			$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		})
	.controller('navigation', function($rootScope, $http, $location, $route) {
			var self = this;
	
			/**
			 * Internal OAUTH token representation.
			 */
			self.oauth2TokenData = {			    
			    	access_token	: null,
			    	token_type		: null,
			    	expires_in		: null,
			    	refresh_token 	: null,
			    	scope			: null
			    };
			
			self.credentials = {};
			
			// ================================================================
	
			/**
			 * Authentication entry point. <p/>
			 * <ul>
			 * <li>Sets up basic HTTP authorizaton headers.</li>
			 * <li>Requests token.</li>
			 * <li>Saves token if authentication was a success.</li>
			 * </ul>
			 */
			self.authenticate = function() {
				var headers = self.credentials ? {
					'Authorization' 	: "Basic "	+ btoa(self.credentials.username + ":" + self.credentials.password),
					'Accept' 			: "application/json",	
					'Content-Type' 		: 'application/x-www-form-urlencoded; charset=utf-8'
				} : {};
				
				var data = 'username='+encodeURIComponent(self.credentials.username)+
							'&password='+encodeURIComponent(self.credentials.password)+
							'&grant_type=password';

				$http.post('/authenticate', data, {'headers': headers}).then(function(response) {
					if (response.data.access_token) {
						self.oauth2TokenData.access_token 	= response.data.access_token;
						self.oauth2TokenData.token_type 		= response.data.token_type;
						self.oauth2TokenData.expires_in 		= response.data.expires_in;
						self.oauth2TokenData.scope 			= response.data.scope;
						self.oauth2TokenData.refresh_token 	= response.data.refresh_token;
						
						$rootScope.authenticated = true;
						$location.path("/");
						self.error = false;
					} else {
						$rootScope.authenticated = false;
						$location.path("/login");
						self.error = true;
					}
				}, function() {
					$rootScope.authenticated = false;
					$location.path("/login");
					self.error = true;
				});
			};

			/**
			 * Token clearance and user logout function.
			 */
			self.logout = function() {
				self.oauth2TokenData.access_token 	= null;
				self.oauth2TokenData.token_type 	= null;
				self.oauth2TokenData.expires_in 	= null;
				self.oauth2TokenData.scope 			= null;
				self.oauth2TokenData.refresh_token 	= null;
				
				self.credentials = {};
				$rootScope.authenticated = false;
				$location.path("/");
				$http.post('logout', {});
			}
		})
	.controller('home', function($http) {
		var self = this;
		$http.get('/resource/').then(function(response) {
			self.greeting = response.data;
	})
});
