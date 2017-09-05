import React, { Component } from 'react';
import './App.css';

class Login extends Component {
	render() {
		return (
			<section>
				<form novalidate class="form-horizontal" ng-submit="login(username, password)">
					<div class='container'>
						<div class='row'>
							<div class='col-md-6 col-md-offset-3'>
								<p><span class='h3'>Login to Argus</span></p>
								<input id='username' type="text" class="form-control" placeholder="Enter username..." ng-model='username' autoFocus /><br/>
								<input id='password' type="password" class="form-control" placeholder="Enter password..." ng-model='password'/>
							</div>
						</div>
						<div class='row'>
							<div class='col-md-6 col-md-offset-3 text-left'>
								<br/>
								<input class="btn btn-primary" type="submit" value="Login" />
							</div>
						</div>
					</div>
				</form>
			<br/>
		</section>
		)
	}
}

export default Login;