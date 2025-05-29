/*
 * Copyright © 2025 rosestack.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/// *
// * Copyright © 2025 Chensoul, Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package cc.chensoul.rose.upms;
//
// import util.security.rose.rosestack.github.io.SecurityUser;
// import domain.contact.upms.rose.rosestack.github.io.UserService;
// import account.domain.upms.rose.rosestack.github.io.Credential;
// import contact.domain.upms.rose.rosestack.github.io.User;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.AuthenticationServiceException;
// import org.springframework.security.core.authority.AuthorityUtils;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
//
/// **
// *
// *
// * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
// * @since
// */
// @Service
// @RequiredArgsConstructor
// public class UpmsUserDetailsService implements UserDetailsService {
//
//	private final UserService userService;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userService.findUserByPhone(username);
//		if (user == null) {
//			throw new AuthenticationServiceException("用户名不存在");
//		}
//
//		Credential credential = userService.findUserCredentialByUserId(user.getId());
//		if (credential == null) {
//			throw new AuthenticationServiceException("用户凭证不存在");
//		}
//
//		return new SecurityUser(
//			user.getName(),
//			credential.getPassword(),
//			AuthorityUtils.createAuthorityList(user.getAuthority().name()));
//	}
// }
