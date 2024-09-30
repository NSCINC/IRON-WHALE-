#!/bin/bash

# Configuration variables
CMAKE_MIN_VERSION="3.18"
PROJECT_NAME="torch"
TORCH_SRC_DIR="$(pwd)"
TORCH_ROOT="${TORCH_SRC_DIR}/.."
BUILD_DIR="build"
INSTALL_DIR="${BUILD_DIR}/lib"
USE_CUDA=ON
USE_ASAN=OFF
USE_TSAN=OFF
USE_ROCM=OFF
USE_XPU=OFF
USE_CUDNN=OFF
USE_COREML_DELEGATE=OFF
USE_NUMPY=OFF
USE_UCC=OFF
BUILD_TEST=OFF
USE_DISTRIBUTED=OFF
USE_NCCL=OFF
USE_MPI=OFF
USE_VALGRIND=OFF
USE_MPS=OFF
CMAKE_BUILD_TYPE="Release"

# Create build directory
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR" || exit

# Run CMake configuration
cmake .. \
    -DCMAKE_BUILD_TYPE="${CMAKE_BUILD_TYPE}" \
    -DCMAKE_CXX_STANDARD=14 \
    -DCMAKE_CXX_STANDARD_REQUIRED=ON \
    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
    -DUSE_CUDA="${USE_CUDA}" \
    -DUSE_ASAN="${USE_ASAN}" \
    -DUSE_TSAN="${USE_TSAN}" \
    -DUSE_ROCM="${USE_ROCM}" \
    -DUSE_XPU="${USE_XPU}" \
    -DUSE_CUDNN="${USE_CUDNN}" \
    -DUSE_COREML_DELEGATE="${USE_COREML_DELEGATE}" \
    -DUSE_NUMPY="${USE_NUMPY}" \
    -DUSE_UCC="${USE_UCC}" \
    -DBUILD_TEST="${BUILD_TEST}" \
    -DUSE_DISTRIBUTED="${USE_DISTRIBUTED}" \
    -DUSE_NCCL="${USE_NCCL}" \
    -DUSE_MPI="${USE_MPI}" \
    -DUSE_VALGRIND="${USE_VALGRIND}" \
    -DUSE_MPS="${USE_MPS}" \
    -DCMAKE_INSTALL_PREFIX="${INSTALL_DIR}"

# Build the project
make -j$(nproc)

# Install the project
make install

# Generate version file
echo "Generating version.py"
python "${TORCH_ROOT}/tools/generate_torch_version.py" \
    --is-debug=$( [ "${CMAKE_BUILD_TYPE}" == "Debug" ] && echo 1 || echo 0 ) \
    --cuda-version=$( [ "${USE_CUDA}" == "ON" ] && echo "your_cuda_version" || echo "none" ) \
    --hip-version=$( [ "${USE_ROCM}" == "ON" ] && echo "your_hip_version" || echo "none" )

echo "Build and installation completed."
