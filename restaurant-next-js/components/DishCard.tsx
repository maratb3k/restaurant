import Image from "next/image";

export default function DishCard({
  disabled = false
}: {
  disabled: boolean
}) {
  return (
    <div className="relative dish-card flex flex-col items-center gap-[16px] group-hover:gap-[8px] group">
      {/* Disabled state indicator */}
      {disabled && (
        <span className="absolute right-[24px] top-[24px] px-[4px] bg-[var(--red-100)] rounded-[8px]">
          <p className="caption-text">On Stop</p>
        </span>
      )}

      <div className={`relative overflow-hidden rounded-full
          ${disabled ? "w-[196px] h-[196px]" : "w-[196px] h-[196px] group-hover:w-[152px] group-hover:h-[152px]"}
          ${disabled ? "opacity-35" : ""}`
      }>
        <Image
          src="/Dish1.png"
          alt="Dish"
          fill
          style={{ objectFit: "cover", objectPosition: "center" }}
        />
      </div>

      <div className={`flex flex-col items-start w-full gap-[4px] ${disabled ? "opacity-35" : ""}`}>
        <p className="body-bold-text text-[14px] text-[--var(--neutral-900)]">
          Fresh Strawberry Mint Salad
        </p>
        <div className="flex flex-row justify-between items-center w-full caption-text text-[--var(--neutral-900)]">
          <p>17$</p>
          <p>430 g</p>
        </div>
      </div>

      {!disabled && (
        <button className="btn w-full !hidden group-hover:!block">
          Pre-order
        </button>
      )}
    </div>
  );
}