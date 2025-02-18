from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser

class CustomUserAdmin(UserAdmin):
    model = CustomUser
    list_display = ("username", "email", "is_staff", "is_superuser", "date_joined")
    list_filter = ("is_staff", "is_superuser", "is_active")
    search_fields = ("username", "email")
    readonly_fields = ("date_joined", "last_login")

    fieldsets = (
        ("User Info", {"fields": ("username", "email", "password")}),
        ("Personal Info", {"fields": ("profile_picture", "phone_number", "date_of_birth")}),
        ("Permissions", {"fields": ("is_staff", "is_superuser", "is_active", "groups", "user_permissions")}),
        ("Important Dates", {"fields": ("last_login", "date_joined")}),
    )

admin.site.register(CustomUser, CustomUserAdmin)