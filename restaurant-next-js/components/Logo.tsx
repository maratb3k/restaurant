import Image from "next/image";

export default function Logo() {
  return (
    <div className="flex flex-row items-center gap-[12px]">
      <Image src="/logo.svg" alt="Logo" width={48} height={48} />
      <span className="logo-text">Green <span className="logo-text-dark">& Tasty</span></span>
    </div>
  );
}