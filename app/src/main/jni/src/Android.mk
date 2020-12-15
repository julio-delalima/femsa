LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := main

SDL_PATH := ../SDL2

LOCAL_CPP_FEATURES += exceptions

LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(SDL_PATH)/include\
	$(LOCAL_PATH)/../SDL_ttf \
	$(LOCAL_PATH)/../SDL_mixer \
    $(LOCAL_PATH)/libpng \
	$(LOCAL_PATH)/zlib \
	$(LOCAL_PATH)/sqrat \
	$(LOCAL_PATH)/sqrat/squirrel \
	$(LOCAL_PATH)/SDL2_gfx \
	$(LOCAL_PATH)/Box2D \

# Add your application source files here...
#traverse all the directory and subdirectory
define walk
  $(wildcard $(1)) $(foreach e, $(wildcard $(1)/*), $(call walk, $(e)))
endef

#find all the file recursively under jni/
ALLFILES = $(call walk, $(LOCAL_PATH))
FILE_LIST_CPP := $(filter %.cpp, $(ALLFILES))
FILE_LIST_C := $(filter %.c, $(ALLFILES))

LOCAL_SRC_FILES := $(SDL_PATH)/src/main/android/SDL_android_main.c \
    $(FILE_LIST_CPP:$(LOCAL_PATH)/%=%) \
    $(FILE_LIST_C:$(LOCAL_PATH)/%=%) \

LOCAL_SHARED_LIBRARIES := SDL2 SDL2_ttf SDL2_mixer

LOCAL_LDLIBS := -lGLESv1_CM -lGLESv2 -llog

include $(BUILD_SHARED_LIBRARY)
