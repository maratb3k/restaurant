import Image from "next/image";
import Star from "@/icons/Star";
import StarOutlined from "@/icons/StarOutlined";

export default function ReviewCard() {
  return (
    <div className="flex flex-col rounded-[24px] w-[316px] bg-[var(--neutral-0)] p-[24px]">
      <div className="flex flex-row gap-[13px] pb-[24px] items-center">
        <div className="bg-[var(--neutral-400)] w-[60px] h-[60px] rounded-full p-[9px]">
          <Image
            src="/Avatar.svg"
            alt="Avatar Image"
            width={42}
            height={42}
          />
        </div>
        <div className="flex flex-row justify-between items-center w-[195px] h-[47px]">
          <div className="flex flex-col gap-[7px]">
            <p className="body-bold-text">User-135</p>
            <p className="caption-text">6/8/2024</p>
          </div>
          <div className="flex flex-row gap-[4px] self-start">
            <Star className="!w-[16px] !h-[16px]" />
            <Star className="!w-[16px]  !h-[16px]" />
            <Star className="!w-[16px]  !h-[16px]" />
            <StarOutlined className="!w-[16px]  !h-[16px]" />
            <StarOutlined className="!w-[16px]  !h-[16px]" />
          </div>
        </div>
      </div>
      <div>
        <p className="body-text">Absolutely loved this restaurant! The outdoor terrace was perfect
          for a relaxing evening, and the menu had so many fresh, healthy options.
          I’m vegetarian, and it’s great to see so many plant-based dishes with
          authentic Georgian flavors. Definitely coming back soon!</p>
      </div>
    </div>
  );
}