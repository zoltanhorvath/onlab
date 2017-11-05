package com.settlerofcatan.gateway.dto;

public class ApplicationUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String nickname;
    private String password;
    private ApplicationUserRoleDTO role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ApplicationUserRoleDTO getRole() {
        return role;
    }

    public void setRole(ApplicationUserRoleDTO role) {
        this.role = role;
    }

    @Override
    public String toString() {
        String roleName = "No role";
        if(role != null){
            roleName = role.getName().name();
        }
        return "ApplicationUserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", role=" + roleName +
                '}';
    }
}
