const LoadingSpinner = ({ text = "Betöltés...", size = "default" }) => {
  const sizeClasses = {
    small: "w-4 h-4",
    default: "w-11 h-11",
  }

  return (
    <div className="flex flex-col justify-center items-center h-[50vh] gap-4">
      <div className={`relative ${sizeClasses[size]} animate-spinner [transform-style:preserve-3d]`}>
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:translateZ(22px)]" />
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:translateZ(-22px)_rotateY(180deg)]" />
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:rotateY(-270deg)_translateX(50%)] [transform-origin:top_right]" />
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:rotateY(270deg)_translateX(-50%)] [transform-origin:center_left]" />
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:rotateX(90deg)_translateY(-50%)] [transform-origin:top_center]" />
        <div className="absolute w-full h-full border-2 border-primary bg-primary/20 [transform:rotateX(-90deg)_translateY(50%)] [transform-origin:bottom_center]" />
      </div>
      {text && <span className="text-lg">{text}</span>}
    </div>
  )
}

export default LoadingSpinner