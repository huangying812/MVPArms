/*
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sl.recharge.call.mvp.model.entity;

/**
 * ================================================
 * User 实体类
 * <p>
 * at 04/09/2016 17:14
 * ================================================
 */
public class User {
    private final int id;
    private final String login;
    private final String avatar_url;
    private final String html_url;

    public User(int id, String login, String avatar_url, String html_url) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
    }

    public String getAvatarUrl() {
        if (avatar_url.isEmpty()) return avatar_url;
        return avatar_url.split("\\?")[0];
    }

    public String getHomePage() {
        return html_url;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override public String toString() {
        return "id -> " + id + " login -> " + login;
    }
}
